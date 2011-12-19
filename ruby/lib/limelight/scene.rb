#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

require 'limelight/java_util'
require 'limelight/prop'
require 'limelight/limelight_exception'
require 'limelight/player_recruiter'

module Limelight

  # A Scene is a root Prop.  Scenes may be loaded onto a Stage.  In addition to being a Prop object, Scenes have a
  # few extra attributes and behaviors.
  #
  class Scene < Prop

    class << self
      def panel_class #:nodoc:
        Java::limelight.ui.model.ScenePanel
      end
    end

    include Java::limelight.model.api.SceneProxy

    attr_reader :player_recruiter

    def initialize(options={})
      @player_recruiter = options.delete(:player_recruiter) || PlayerRecruiter.new
      super(options)
      @peer.player_recruiter = @player_recruiter
      @fs = Java::limelight.Context.fs
    end

    def production=(value)
      @peer.production = value.peer
    end

    def production
      @peer.production.proxy
    end

    def button_groups
      @peer.button_groups
    end

    def on_scene_opened(& action)
      @peer.event_handler.add(Java::limelight.ui.events.panel.SceneOpenedEvent, action)
    end

    # Returns a hash of all the styles belonging to this scene
    #
    def styles
      if @styles == nil
        styles = @peer.styles
        @styles = Util::Hashes.for_ruby(styles)
      end
      @styles
    end

    # Returns self.  A Scene is it's own scene.
    #
    def scene
      self
    end

    # Returns the stage that contains the scene
    #
    def stage
      @peer.stage.proxy
    end

    # Returns the path to the root directory of the Scene
    #
    def path
      @peer.path
    end

    # Returns the path to the Scene's props file
    #
    def props_file
      @fs.path_to(path, "props.rb")
    end

    # Returns the path to the Scene's props file
    #
    def styles_file
      @fs.path_to(path, "styles.rb")
    end

    # Creates the menu bar for the Scene
    #
    def menu_bar
      DSL::MenuBar.build(self) do
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
      peer_result.nil? ? nil : peer_result.proxy
    end

    def visible?
      @peer.visible?
    end

  end
end