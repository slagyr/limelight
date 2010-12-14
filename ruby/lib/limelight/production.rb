#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/limelight_exception'
require 'limelight/dsl/styles_builder'
require 'limelight/theater'
require 'limelight/dsl/stage_builder'
require 'limelight/dsl/prop_builder'
require 'limelight/dsl/styles_builder'
require 'limelight/util/hashes'
require 'limelight/scene'
require 'drb'


module Limelight

  # The root object of Limelight Production.  Every Prop in a production has access to its Production object.
  # Therefore it is typical to store reasources in the Production.
  #
  # Productions are configured, and attributes are added, by the ProductionBuilder.
  #
  class Production

    include Java::limelight.model.api.ProductionProxy

    attr_reader :peer
    attr_accessor :theater

    alias :getTheater :theater
    alias :getPeer :peer

    # Users typically need not create Production objects.
    #
    def initialize(production)
      @peer = production
      @theater = Theater.new(self, @peer.theater)

      @peer.proxy = self
    end

    # returns true if the production has been opened, and not yet closed.
    #
    def open?
      return @peer.open?
    end

    # Returns the name of the Production
    #
    def name
      return @peer.name
    end

    # Sets the name of the Production.  The name must be unique amongst all Productions in memory.
    #
    def name=(value)
      @peer.name = value
    end

    # Returns the resource loader for the Production
    #
    def root
      return @peer.resource_loader
    end

    # Return the path to the root directory of the production
    #
    def path
      return @peer.resource_loader.root
    end

    # Returns the path to the production's init file
    #
    def init_file
      return root.path_to("init.rb")
    end

    # Returns the path to the production's production.rb file
    #
    def production_file
      return root.path_to("production.rb")
    end

    # Returns the path to the production's stages file
    #
    def stages_file
      return root.path_to("stages.rb")
    end

    # Returns the path to the production's styles file
    #
    def styles_file
      return root.path_to("styles.rb")
    end

    # Returns the path to the production's gems directory
    #
    def gems_directory
      return root.path_to("__resources/gems/gems")
    end

    # Returns the path to the productions gems root
    #
    def gems_root
      return root.path_to("__resources/gems")
    end

    # Returns the path to the named Scene's directory within the Production
    #
    def scene_directory(name)
      return root.root if name == nil
      return root.path_to(name)
    end

    # Returns true if the production allows itself to be closed.  The system will call this methods when
    # it wishes to close the production, perhaps when the user quits the application.  By default the production
    # will always return true.
    #
    def allow_close?
      return @peer.allow_close?
    end

    # Specifies whether this production will allow itself to be closed.  It should be noted that permanently setting
    # this to false could keep the entire Limelight runtime from shutting down.
    #
    def allow_close=(value)
      @peer.allow_close = value
    end

    # Specifies the minimum required version of Limelight that should be used to open this production.
    # Defaults to zero, meaning any version of Limelight may open the production.
    #
    def minimum_limelight_version=(value)
      @peer.minimum_limelight_version = value
    end

    # Closes the production. If there are no more productions open, the Limelight runtime will shutdown.
    # The production will actually delegate to it's producer and the producer will close the production down.
    #
    def close
      @peer.close
    end

    # Publish this production using DRb on the specified port.  The production will delegate to its producer to
    # actually do the publishing.
    #
    def publish_on_drb(port)
      @producer.publish_production_on_drb(port)
    end

    # A production with multiple Scenes may have a 'styles.rb' file in the root directory.  This is called the
    # root_styles.  This method loads the root_styles, if they haven't already been loaded, and returns them.
    #
    def root_styles
      unless @root_styles
        if File.exists?(styles_file)
          @root_styles = Limelight.build_styles_from_file(styles_file)
        else
          @root_styles = {}
        end
      end
      return @root_styles
    end

    def on_production_created(&action)
      @peer.event_handler.add(Java::limelight.model.events.ProductionCreatedEvent, action)
    end

    def on_production_loaded(&action)
      @peer.event_handler.add(Java::limelight.model.events.ProductionLoadedEvent, action)
    end

    def on_production_opened(&action)
      @peer.event_handler.add(Java::limelight.model.events.ProductionOpenedEvent, action)
    end

    def on_production_closing(&action)
      @peer.event_handler.add(Java::limelight.model.events.ProductionClosingEvent, action)
    end

    def on_production_closed(&action)
      @peer.event_handler.add(Java::limelight.model.events.ProductionClosedEvent, action)
    end

    def callMethod(name, java_obj_array) #:nodoc:
      args = []
      java_obj_array.length.times { |i| args << java_obj_array[i] }
      send(name.to_sym, * args)
    end

    def open_scene(scene_path, stage, options={})
      @peer.open_scene(scene_path, stage.peer, Util::Hashes.for_java(options))
    end

    def illuminate
      return unless File.exists?(production_file)
      content = IO.read(production_file)
      self.instance_eval(content, production_file)
    end

    def load_libraries
      # use bundler here
    end

    alias :loadLibraries :load_libraries

    def load_stages
      return unless File.exists?(stages_file)
      content = IO.read(stages_file)
      Limelight.build_stages(theater) do
        begin
          eval content
        rescue Exception => e
          raise DSL::BuildException.new(stages_file, content, e)
        end
      end
    end

    alias :loadStages :load_stages

    def load_scene(scene_path, options={})
      options = Util::Hashes.for_ruby(options)
      instance_variables = options.delete(:instance_variables)
      full_scene_path = scene_directory(scene_path)
      scene = Scene.new(options.merge(:path => full_scene_path, :name => File.basename(full_scene_path)))
      scene.production = self
      if File.exists?(scene.props_file)
        content = IO.read(scene.props_file)
        options[:build_loader] = self.root
        Limelight.build_props(scene, options.merge(:instance_variables => instance_variables)) do
          begin
            eval content
          rescue Exception => e
            raise DSL::BuildException.new(scene.props_file, content, e)
          end
        end
      end
      return scene
    end

    alias :loadScene :load_scene

    def load_styles(path, extendable_styles)
#      builtin_styles = Util::Hashes.for_ruby(BuiltIn::Styles.all)
#      extendable_styles = builtin_styles.merge(root_styles)
#      scene.styles.merge!(extendable_styles)
      result = {}
      styles_path = "#{path}/styles.rb"
      if File.exists?(styles_path)
        Limelight.build_styles_from_file(styles_path, :styles => result, :extendable_styles => extendable_styles)
      end
      return result
    end

    alias :loadStyles :load_styles

  end

end