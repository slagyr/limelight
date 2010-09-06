#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

#require 'limelight/dsl/menu_bar'
require 'limelight/file_chooser'
require 'limelight/util'

module Limelight

  # In the metephorical sense, a Stage is a platform which may hold a scene.  In a more, to-the-point sence, a Stage
  # represents a window on the computer screen.  The Stage objects within a Production are configured by the StagesBuilder.
  # By default a Production will have one Stage.  A Stage may open any number of Scenes but it may only have one current
  # scene loaded at a time.
  #
  class Stage
    attr_accessor :default_scene
    attr_reader :frame, :current_scene, :name, :theater

    attr_accessor :should_remain_hidden #:nodoc:

    include UI::Api::Stage

    # To create a new Stage, it be given a Theater to which it belongs, and the name is optional.  If no name is provided
    # it will default to 'default'.  A stage name must be unique, so it is recommended you provide a name.
    #
    def initialize(theater, name, options = {})
      @theater = theater
      @name = name
      build_frame
      self.title = @name
      apply_options(options)
    end

    # Returns the title that is displayed at the top of the window that this stage represents.
    #
    def title
      return @frame.title
    end

    # Sets the title that is displayed at the top of the window that this stage represents. 
    #
    def title=(value)
      @frame.title = value
    end

    # Returns the width and height styles of the Stage.
    #
    #   width, height = stage.size
    #
    def size
      return @frame.width_style.to_s, @frame.height_style.to_s
    end

    # Sets the width and height styles of the Stage.
    #
    #   state.size = [100, 200]
    #   state.size = ["50%", "100%"] # consuming 50% of the width and 100% of the height of the screen
    #
    def size=(*values)
      values = values[0] if values.size == 1 && values[0].is_a?(Array)
      @frame.set_size_styles(values[0], values[1])
    end

    # The location styles of the Stage.
    #
    #   x, y = stage.location
    #
    def location
      return @frame.getXLocationStyle.to_s, @frame.getYLocationStyle.to_s
    end

    # Sets the location styles of the Stage.
    #
    #   stage.location = [500, 200]
    #
    def location=(*values)
      values = values[0] if values.size == 1 && values[0].is_a?(Array)
      @frame.set_location_styles(values[0], values[1])
    end

    # Turns fullscreen mode on and off.  In fullscreen mode, the stage will fill the entire screen and appear on top
    # of all other windows.
    #
    #   stage.fullscreen = true
    #

    def fullscreen=(on)
      @frame.setFullScreen(on)
    end

    # Returns true if the stage is in fullscreen mode.
    #
    def fullscreen?
      return @frame.isFullScreen()
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
      @frame.setKiosk(on)
    end

    # Return true if the stage is in kiosk mode.
    #
    def kiosk?
      return @frame.isKiosk()
    end


    # Hides the stage so that it is not visible on the screen without destroying it.
    #
    def hide
      @frame.visible = false
    end

    # Shows the stage after having hiding it.
    #
    def show
      @frame.visible = true unless @should_remain_hidden
    end

    # Returns true if the stage is visible on the screen.
    #
    def visible?
      return @frame.visible
    end

    # Sets the background color of the stage
    #
    def background_color=(value)
      @frame.background_color = value.to_s
    end

    # Returns the background color of the stage in the format #RRGGBB(AA)
    #
    def background_color
      return @frame.background_color
    end

    # When true, the stage will be frame with the operating system's standard window frame including close, minimize,
    # and maximize buttons
    #
    def framed=(value)
      @frame.setUndecorated(!value)
    end

    # Return true if the stage is framed.
    #
    def framed?
      return !@frame.isUndecorated()
    end

    # When true, the stage will remain on top of all other windows.
    #
    def always_on_top=(value)                                                                              
      @frame.always_on_top = value
    end

    # Return true if the stage has been set to always be on top.
    #
    def always_on_top?
      return @frame.always_on_top
    end

    # Sets the vitality of the stage.  Limelight will not exit while vital frames remain visible.
    #
    def vital=(value)
      @frame.vital = value
    end

    # Returns true if this is a vital stage.  Limelight will not exit while vital frames remain visible.
    #
    def vital?
      return @frame.vital?
    end

    # Opens the Stage and loads the provided Scene.
    #
    # See load_scene
    #
    def open(scene)
      @current_scene.visible = false if @current_scene
      scene.stage = self
      scene.illuminate
      load_scene(scene)
      @frame.open unless @should_remain_hidden
      scene.visible = true
      scene.panel.event_handler.dispatch(Limelight::UI::Events::SceneOpenedEvent.new(scene.panel))
    end

    # Closes the Stage. It's window will no longer be displayed on the screen.
    #
    def close
      @frame.close
    end

    # Loads a scene on the Stage.  If the Stage is currently hosting a Scene, the original Scene will be removed and
    # the new Scene will replace it.
    #
    def load_scene(scene)      
      #      @frame.setJMenuBar(scene.menu_bar)
      @frame.load(scene.panel)
      if (has_static_size?(scene.style))
        @frame.set_size(scene.style.compiled_width.value + @frame.getHorizontalInsetWidth, scene.style.compiled_height.value + @frame.getVerticalInsetWidth)
      end
      @current_scene = scene
    end

    # Opens a file chooser window to select a file on the file system. Options may include:
    # * :description => a string describing the desired file
    # * :directory => starting directory
    # * :title => title of the window
    # * :directories_only => boolean, true if the user must select a directory
    # * :files_only => boolean, true if the user must select a file
    #
    def choose_file(options={}, &block)
      options[:parent] = @frame
      chooser = FileChooser.new(options, &block)
      return chooser.choose_file
    end

    # Pops up a simple modal dialog with the provided message.
    #
    def alert(message)
      Thread.new do
        begin
          Context.instance.studio.utilities_production.alert(message)
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
      return true if @current_scene.nil?
      return true if !@current_scene.respond_to?(:allow_close?)
      return @current_scene.allow_close?
    end

    def stub_current_scene(scene) #:nodoc:
      @current_scene = scene
    end

    # returns true if the stage has been closed.  Closed stages may not be reopened.
    #
    def closed?
      return @frame.closed?
    end

    # Invoked when the stage is being closed.
    # System hook that should NOT be called by you.
    #
    def closing(e)
    end

    # Invoked when the stage has been closed.
    # System hook that should NOT be called by you.
    # It's not garunteed that this hook will be called when Limelight is shutting down.  
    #
    def closed(e)
      @current_scene.visible = false if @current_scene
      @current_scene = nil
      @theater.stage_closed(self)
    end

    # Invoked when the stage has gained focus on the desktop.  Only 1 stage my have focus at a time.
    # System hook that should NOT be called by you.
    #
    def focus_gained(e)
      @theater.stage_activated(self)
    end

    # Invoked when the stage has lost focus on the desktop.  Only 1 stage my have focus at a time.
    # System hook that should NOT be called by you.
    #
    def focus_lost(e)
    end

    # Invoked when the stage has been iconified.  This occurs when the stage is no longer visible on the desktop
    # and an icon for the stage has been added to the OS's taskbar or dock.
    # System hook that should NOT be called by you.
    #
    def iconified(e)
    end

    # Invoked when the stage has been deiconified.  This occurs when the icon for the stage has been removed from the
    # taskbar or dock, and the stage is again visible on the desktop.  
    # System hook that should NOT be called by you.
    #
    def deiconified(e)
    end

    # Invoked when the stage has become the active stage on the desktop.  Only 1 stage my be active at a time.
    # System hook that should NOT be called by you.
    #
    def activated(e)
      @theater.stage_activated(self)
    end

    # Invoked when the stage has lost status as the active stage.  Only 1 stage my have focus at a time.
    # System hook that should NOT be called by you.
    #
    def deactivated(e)    
      @theater.stage_deactivated(self)  
    end

    protected #############################################

    def new_frame
      return UI::Model::Frame.new(self)
    end

    private ###############################################

    def build_frame
      @frame = new_frame
      @frame.set_size(800, 800)
      @frame.set_location(200, 25)
      @frame.title = title
    end

    def has_static_size?(style)
      return is_static?(style.get_width) && is_static?(style.get_height)
    end

    def is_static?(value)
      return !(value.to_s.include?("%")) && !(value.to_s == "auto")
    end

    def apply_options(options)
      options.each_pair do |key, value|
        setter_sym = "#{key.to_s}=".to_sym
        if self.respond_to?(setter_sym)
          self.send(setter_sym, value)    
        end
      end
    end

  end

end

