#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/file_loader'
require 'limelight/prop_builder'
require 'limelight/styles_builder'
require 'limelight/stage_builder'
require 'limelight/production_builder'
require 'limelight/casting_director'
require 'limelight/stage'
require 'limelight/build_exception'
require 'limelight/theater'
require 'limelight/production'

module Limelight

  # A Producer has the hefty responsibility of producing Productions.  Given a directory, it will load the neccessary
  # files and create all the neccessary objects to bring a Production to life.
  #
  # For directory structures, see Limelight::Main
  #
  class Producer

    # Creates a new Producer and has it open a Production by specified name.
    #
    def self.open(production_name)   
      producer = new(production_name)
      producer.open
    end
    
    attr_reader :loader, :theater, :production
    attr_writer :builtin_styles

    # A Production name, or root directory, must be provided. If not Theater is provided, one will be created.
    # You may also provide an existing Production for which this Producer will interact.
    #
    def initialize(root_path, theater=nil, production=nil)
      if(root_path[-4..-1] == ".llp")
        root_path = unpack_production(root_path)
      end
      @loader = FileLoader.for_root(root_path)
      @theater = theater.nil? ? Theater.new : theater
      @production = production
    end

    # Returns the CastingDirector for this Production.
    #
    def casting_director
      @casting_director = CastingDirector.new(loader) if not @casting_director
      return @casting_director
    end

    # Opens the Production specified during construction. If the file 'init.rb' exists in the root directory of the
    # Production, it will be loaded before anything else.
    #
    def open()
      establish_production
      Kernel.load(@loader.path_to("init.rb")) if @loader.exists?("init.rb")
      if @loader.exists?("stages.rb")
        load_stages.each { |stage| open_scene(stage.default_scene, stage) }
      else
        open_scene(@loader.root, @theater.default_stage)
      end
      @casting_director = nil
    end

    # Opens the specified Scene onto the Spcified Stage.
    #
    def open_scene(path, stage)
      styles = load_styles(path)
      merge_with_root_styles(styles)

      scene_name = path.nil? ? "default" : File.basename(path)
      scene = load_props(path, :styles => styles, :production => @production, :casting_director => casting_director, :loader => @loader, :path => path, :name => scene_name)

      stage.open(scene)
    end

    # Loads the 'stages.rb' file and configures all the Stages in the Production.
    #
    def load_stages
      content = @loader.load("stages.rb")
      stages = Limelight.build_stages(@theater) do
        begin
          eval content
        rescue Exception => e
          raise BuildException.new("stages.rb", content, e)
        end
      end
      return stages
    end

    # Loads of the 'props.rb' file for a particular Scene and creates all the Prop objects and Scene.
    #
    def load_props(path, options = {})
      return Scene.new(options) if path.nil?
      filename = File.join(path, "props.rb")
      content = @loader.exists?(filename) ? @loader.load(filename) : ""
      options[:build_loader] = @loader
      return Limelight.build_scene(options) do
        begin
          eval content
        rescue Exception => e
          raise BuildException.new(filename, content, e)
        end
      end
    end

    # Loads the specified 'styles.rb' file and created a Hash of Styles.
    #
    def load_styles(path)
      styles = builtin_styles
      return styles if path.nil?
      filename = File.join(path, "styles.rb")
      return styles if not @loader.exists?(filename)
      content = @loader.load(filename)
      return Limelight.build_styles(styles) do
        begin
          eval content
        rescue Exception => e
          raise BuildException.new(filename, content, e)
        end
      end
    end

    # Loads the 'production.rb' file if it exists and configures the Production.
    #
    def establish_production
      return if @production
      if @loader.exists?("production.rb")
        content = @loader.load("production.rb")
        @production = Limelight.build_production(@loader.root, self, @theater) do
          begin
            eval content
          rescue Exception => e
            raise BuildException.new("production.rb", content, e)
          end
        end
      else
        @production = Production.new(@loader.root, self, @theater)
      end      
    end

    # A production with multiple Scenes may have a 'styles.rb' file in the root directory.  This is called the
    # root_style.  This method loads the root_styles, if they haven't already been loaded, and returns them.
    #
    def root_styles
      return @root_syles if @root_syles
      if @loader.exists?('styles.rb')
        @root_styles = load_styles('.')
      else
        @root_styles = {}
      end
      return @root_styles
    end
    
    private ###############################################
    
    def merge_with_root_styles(styles)
      root_styles.each_pair do |key, value|
        styles[key] = value if !styles.has_key?(key)
      end
    end

    def unpack_production(production_name)
      packer = Limelight::Util::Packer.new()
      return packer.unpack(production_name)
    end

    def builtin_styles
      return @builtin_styles if @builtin_styles 
      builtin_styles_file = File.join($LIMELIGHT_LIB, "limelight", "builtin", "styles.rb")
      content = IO.read(builtin_styles_file)
      @builtin_styles = Limelight.build_styles do
        begin
          eval content
        rescue Exception => e
          raise BuildException.new(filename, content, e)
        end
      end
      return @builtin_styles
    end

  end
  
end