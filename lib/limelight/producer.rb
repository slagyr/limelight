#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/file_loader'
require 'limelight/dsl/prop_builder'
require 'limelight/dsl/styles_builder'
require 'limelight/dsl/stage_builder'
require 'limelight/casting_director'
require 'limelight/stage'
require 'limelight/dsl/build_exception'
require 'limelight/theater'
require 'limelight/production'
require 'limelight/gems'
require 'limelight/util/downloader'
require 'limelight/version'
require 'drb'

module Limelight

  # A Producer has the hefty responsibility of producing Productions.  Given a directory, it will load the neccessary
  # files and create all the neccessary objects to bring a Production to life.    A producer will produce only
  # one production.
  #
  # For directory structures, see Limelight::Main
  #
  class Producer

    class << self
      # Creates a new Producer and has it open a Production by specified name.
      #
      def open(production_name, options={})
        producer = new(production_name)
        begin
          producer.open(options)
        rescue Exception => e
          puts e
          puts e.backtrace
        end
      end

      # Returns a hash of all the built-in Limglight Styles
      #
      def builtin_styles
        unless @builtin_styles
          @builtin_styles = {}
          BuiltIn::Styles.all.key_set.each do |name|
            @builtin_styles[name] = BuiltIn::Styles.all.get(name);
          end
        end
        return @builtin_styles
      end
    end

    attr_reader :theater, :production, :drb_service
    attr_writer :builtin_styles

    # A Production name, or root directory, must be provided. If not Theater is provided, one will be created.
    # You may also provide an existing Production for which this Producer will interact.
    #
    def initialize(root_path, theater=nil, production=nil)
      if (root_path[-4..-1] == ".lll")
        url = IO.read(root_path).strip
        root_path = Util::Downloader.download(url)
      end
      if (root_path[-4..-1] == ".llp")
        root_path = unpack_production(root_path)
      end
      @production = production || Production.new(root_path)
      @theater = theater.nil? ? Theater.new(@production) : theater
      establish_production
    end

    # Returns true if the production's minimum_limelight_version is compatible with the current version.
    #
    def version_compatible?
      current_version = Limelight::Util::Version.new(Limelight::VERSION::STRING)
      required_version = Limelight::Util::Version.new(@production.minimum_limelight_version)
      return required_version.is_less_than_or_equal(current_version)
    end

    # Returns the CastingDirector for this Production.
    #
    def casting_director
      @casting_director = CastingDirector.new(@production.root) if not @casting_director
      return @casting_director
    end

    # Loads the Production without opening it.  The Production will be created into memory with all it's stages
    #
    def load(options = {})
      Gems.install_gems_in_production(@production)
      Kernel.load(@production.init_file) if ( !options[:ignore_init] &&  File.exists?(@production.init_file) )
      load_stages if File.exists?(@production.stages_file)
    end

    # Returns true if the production is compatible with the current version of Limelight or if the user proceeds
    # despite the incompatible warning.
    #
    def can_proceed_with_compatibility?
      return true if version_compatible?
      return true if Context.instance.studio.utilities_production.should_proceed_with_incompatible_version(@production.name, @production.minimum_limelight_version)
      return false
    end

    # Opens the Production.
    #
    def open(options = {})
      @production.production_opening
      load
      @production.production_loaded
      if @theater.has_stages?
        @theater.stages.each do |stage|
          open_scene(stage.default_scene.to_s, stage) if stage.default_scene
        end
      elsif @production.default_scene
        open_scene(@production.default_scene, @theater.default_stage)
      end
      @casting_director = nil
      @production.production_opened
    end

    # Opens the specified Scene onto the Spcified Stage.
    #
    def open_scene(name, stage, options={})
      path = @production.scene_directory(name)
      scene_name = File.basename(path)
      scene = load_props(options.merge(:production => @production, :casting_director => casting_director, :path => path, :name => scene_name))
      load_styles(scene.styles_file, scene.styles_store)
      stage.open(scene)
      return scene
    end

    # Loads the 'stages.rb' file and configures all the Stages in the Production.
    #
    def load_stages
      stages_file = @production.stages_file
      content = IO.read(stages_file)
      stages = Limelight.build_stages(@theater) do
        begin
          eval content
        rescue Exception => e
          raise DSL::BuildException.new(stages_file, content, e)
        end
      end
      return stages
    end

    # Loads of the 'props.rb' file for a particular Scene and creates all the Prop objects and Scene.
    #
    def load_props(options = {})
      scene = Scene.new(options)
      if File.exists?(scene.props_file)
        content = IO.read(scene.props_file)
        options[:build_loader] = @production.root
        return Limelight.build_props(scene, options) do
          begin
            eval content
          rescue Exception => e
            raise DSL::BuildException.new(scene.props_file, content, e)
          end
        end
      else
        return scene
      end
    end

    # Loads all the Style objects in the specified 'styles.rb' file and stores them in a Hash.
    #
    def load_styles(styles_file, styles)
      extendable_styles = Producer.builtin_styles.merge(@production.root_styles)   
      styles.merge!(extendable_styles)
      return if not File.exists?(styles_file)
      Limelight.build_styles_from_file(styles_file, :styles => styles, :extendable_styles => extendable_styles)
    end

    # Closes the specified production.  The producer will trigger the hook, production_closing and production_closed,
    # to keep the production aware of it's status.  The Studio will also be informed of the closure.  If no
    # production remain opened, then the Limelight runtine will exit.
    #
    def close
      return if @production.closed?
      @production.closed = true
      return Thread.new do
        begin
          Thread.pass
          @production.production_closing
          @production.theater.close
          @production.production_closed
          @drb_service.stop_service if @drb_service
          Context.instance.studio.production_closed(@production)
        rescue StandardError => e
          puts e
          puts e.backtrace
        end
      end
    end

    # Publish the production, using DRb, on the specified port.  This is useful for testing or remotely controling
    # your production.  Publilshing productions on DRb is typically accomplished by using the --drb_port option
    # of the open command. eg.
    #
    #   jruby -S limelight open --drb_port=9000 my_production
    #
    def publish_production_on_drb(port)
#      puts "publishing production (#{@production.name}) on port: #{port}"
      @drb_service = DRb.start_service("druby://localhost:#{port}", @production)
    end

    def establish_production #:nodoc:
      @production.producer = self
      @production.theater = @theater

      production_file = @production.production_file
      if File.exists?(production_file)
        tmp_module = Module.new
        content = IO.read(production_file)
        tmp_module.module_eval(content, production_file)
        production_module = tmp_module.const_get("Production")
        raise "production.rb should define a module named 'Production'" if production_module.nil?
        @production.extend(production_module)
      end
    end

    private ###############################################

    def unpack_production(production_name)
      packer = Limelight::Util::Packer.new()
      dest_dir = File.join(Data.productions_dir, rand.to_s.sub("0.", ""))
      Dir.mkdir(dest_dir)
      return packer.unpack(production_name, dest_dir)
    end

  end

end