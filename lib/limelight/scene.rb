#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/java_util'
require 'limelight/prop'
require 'limelight/button_group_cache'
require 'limelight/limelight_exception'

module Limelight

  # A Scene is a root Prop.  Scenes may be loaded onto a Stage.  In addition to being a Prop object, Scenes have a
  # few extra attributes and behaviors.
  #
  class Scene < Prop

    class << self
      def panel_class #:nodoc:
        return Java::limelight.ui.model.ScenePanel
      end
    end

    include Java::limelight.model.api.SceneProxy

    attr_reader :cast

    def initialize(options={})
      super(options)    
      @cast = Module.new
    end

    def production=(value)
      @peer.production = value.peer
    end

    def production
      return @peer.production.proxy
    end

    def button_groups
      return @peer.button_groups
    end

    def on_scene_opened(& action)
      @peer.event_handler.add(Java::limelight.ui.events.panel.SceneOpenedEvent, action)
    end

    # Returns a hash of all the styles belonging to this scene
    #
    def styles_store
      if @styles_store == nil
        styles_store = @peer.styles_store
        @styles_store = Util::Hashes.for_ruby(styles_store)
      end
      return @styles_store
    end

    # Returns self.  A Scene is it's own scene.
    #
    def scene 
      return self
    end

    # Returns the stage that contains the scene
    #
    def stage
      return @peer.stage.proxy
    end

    # Returns the path to the root directory of the Scene
    #
    def path
      return @peer.resource_loader.root
    end

    # Returns the path to the Scene's props file
    #
    def props_file
      return @peer.resource_loader.path_to("props.rb")
    end

    # Returns the path to the Scene's props file
    #
    def styles_file
      return @peer.resource_loader.path_to("styles.rb")
    end

    # Creates the menu bar for the Scene 
    #
    def menu_bar
      return DSL::MenuBar.build(self) do
        menu("File") do
          item("Open", :open_chosen_production)
          item("Refresh", :reload)
        end
      end
    end

    # Creates a new Producer to open the specified Production.
    #
    def open_production(production_path)
      Thread.new { Context.instance.studio.open(production_path) }
    end

    # Opens the specified Scene on the Stage currently occupied by this Scene.
    # TODO It doesn't quite make sense that a scene loads other scene.  It has to replace itself?
    #
    def load(scene_name)
      production.open_scene(scene_name, stage)
    end

    # Returns a Prop with the specified id.  Returns nil id the Prop doesn't exist in the Scene. 
    #
    def find(id)
      peer_result = @peer.find(id.to_s)
      return peer_result.nil? ? nil : peer_result.proxy
    end

    def visible?
      return @peer.visible?
    end

  end
end