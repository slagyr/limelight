#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

#require 'limelight/dsl/menu_bar'
require 'limelight/optionable'
require 'limelight/file_chooser'
require 'limelight/util'
require 'limelight/util/hashes'

module Limelight

  # In the metephorical sense, a Stage is a platform which may hold a scene.  In a more, to-the-point sence, a Stage
  # represents a window on the computer screen.  The Stage objects within a Production are configured by the StagesBuilder.
  # By default a Production will have one Stage.  A Stage may open any number of Scenes but it may only have one current
  # scene loaded at a time.
  #
  class Stage

    include Java::limelight.model.api.StageProxy
    include Optionable

    attr_reader :peer

    # To create a new Stage, it be given a Theater to which it belongs, and the name is optional.  If no name is provided
    # it will default to 'default'.  A stage name must be unique, so it is recommended you provide a name.
    #
    def initialize(theater, name, options = {})
      @theater = theater
      @peer = Java::limelight.ui.model.FramedStage.new(name, self)
      @peer.apply_options(Util::Hashes.for_java(options))
    end

    # Returns the name of the scene that will be loaded on this stage by default
    #
    def default_scene_name
      return @peer.default_scene_name
    end

    # Specifies the name of the scene that will be loaded on this stage by default
    #
    def default_scene_name=(value)
      @peer.default_scene_name = value
    end

    # Returns the name of the stage
    #
    def name
      return @peer.name
    end

    # Returns the title that is displayed at the top of the window that this stage represents.
    #
    def title
      return @peer.title
    end

    # Sets the title that is displayed at the top of the window that this stage represents.
    #
    def title=(value)
      @peer.title = value
    end

    # Returns the width and height styles of the Stage.
    #
    #   width, height = stage.size
    #
    def size
      return @peer.width_style.to_s, @peer.height_style.to_s
    end

    # Sets the width and height styles of the Stage.
    #
    #   state.size = [100, 200]
    #   state.size = ["50%", "100%"] # consuming 50% of the width and 100% of the height of the screen
    #
    def size=(*values)
      values = values[0] if values.size == 1 && values[0].is_a?(Array)
      @peer.set_size_styles(values[0], values[1])
    end

    # The location styles of the Stage.
    #
    #   x, y = stage.location
    #
    def location
      return @peer.getXLocationStyle.to_s, @peer.getYLocationStyle.to_s
    end

    # Sets the location styles of the Stage.
    #
    #   stage.location = [500, 200]
    #
    def location=(*values)
      values = values[0] if values.size == 1 && values[0].is_a?(Array)
      @peer.set_location_styles(values[0], values[1])
    end

    # Turns fullscreen mode on and off.  In fullscreen mode, the stage will fill the entire screen and appear on top
    # of all other windows.
    #
    #   stage.fullscreen = true
    #
    def fullscreen=(on)
      @peer.setFullScreen(on)
    end

    # Returns true if the stage is in fullscreen mode.
    #
    def fullscreen?
      return @peer.isFullScreen()
    end

    # Turns kiosk mode for this stage on and off.  When on, the stage will:
    #   * be fullscreen
    #   * not be frames
    #   * disable OS key commands
    #     * OS X: Cmd-Tab, Cmd-Alt-Esc, etc...
    #     * Windows: Alt-Tab, Ctrl-Esc, etc... (Ctrl-Alt-Del must be disabled through a registry entry)
    #
    #   stage.kiosk = true
    #
    def kiosk=(on)
      @peer.setKiosk(on)
    end

    # Return true if the stage is in kiosk mode.
    #
    def kiosk?
      return @peer.isKiosk()
    end


    # Hides the stage so that it is not visible on the screen without destroying it.
    #
    def hide
      @peer.hide
    end

    # Shows the stage after having hiding it.
    #
    def show
      @peer.show
    end

    # Returns true if the stage is visible on the screen.
    #
    def visible?
      return @peer.visible
    end

    # Sets the background color of the stage
    #
    def background_color=(value)
      @peer.background_color = value.to_s
    end

    # Returns the background color of the stage in the format #RRGGBB(AA)
    #
    def background_color
      return @peer.background_color
    end

    # When true, the stage will be frame with the operating system's standard window frame including close, minimize,
    # and maximize buttons
    #
    def framed=(value)
      @peer.framed = value
    end

    # Return true if the stage is framed.
    #
    def framed?
      return @peer.framed?
    end

    # When true, the stage will remain on top of all other windows.
    #
    def always_on_top=(value)
      @peer.always_on_top = value
    end

    # Return true if the stage has been set to always be on top.
    #
    def always_on_top?
      return @peer.always_on_top
    end

    # Sets the vitality of the stage.  Limelight will not exit while vital frames remain visible.
    #
    def vital=(value)
      @peer.vital = value
    end

    # Returns true if this is a vital stage.  Limelight will not exit while vital frames remain visible.
    #
    def vital?
      return @peer.vital?
    end

    # Loads the provided Scene.
    #
    # See load_scene
    #
    def scene=(scene)
      @peer.scene = scene.peer
    end

    # Returns the scene currently open on the stage
    #
    def scene
      return @peer.scene.proxy if @peer.scene
    end

    # Returns the theater that the stage belongs to.
    #
    def theater
      return @peer.theater.proxy
    end

    # Opens the stage. Opened stages are visible on the screen.
    #
    def open
      @peer.open
    end

    # Closes the Stage. It's window will no longer be displayed on the screen.
    #
    def close
      @peer.close
    end

    # Pops up a simple modal dialog with the provided message.
    #
    def alert(message)
      Thread.new do
        begin
          Java::limelight.Context.instance.studio.utilities_production.alert(message)
        rescue StandardError => e
          puts "Error on alert: #{e}"
        end
      end
    end

    # Called when the user or system would like to close the stage.  The stage will return true, allowing it to be
    # closed, unless the current_scene defines the allow_close? method in which case the decision it left up to the
    # current_scene.
    #
    def should_allow_close
      return @peer.should_all_close
    end

    # returns true if the stage has been closed.  Closed stages may not be reopened.
    #
    def closed?
      return @peer.closed?
    end

    def should_remain_hidden #:nodoc:
      return @peer.should_remain_hidden
    end

    def should_remain_hidden=(value) #:nodoc:
      @peer.should_remain_hidden = value
    end
  end

end

