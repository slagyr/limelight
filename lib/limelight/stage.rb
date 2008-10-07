#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/menu_bar'
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
    
    include UI::Api::Stage

    # To create a new Stage, it be given a Theater to which it belongs, and the name is optional.  If no name is provided
    # it will default to 'default'.  A stage name must be unique, so it is recommended you provide a name.
    #
    def initialize(theater, name="default")
      @theater = theater
      @name = name
      build_frame
      self.title = @name
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

    # Returns the width and height of the Stage.
    #
    #   width, height = stage.size
    #
    def size
      return @frame.width, @frame.height
    end

    # Sets the width and height of the Stage. The dimensions are passed in as an array.
    #
    #   state.size = [100, 200]
    #
    def size=(values)
      @frame.set_size(values[0], values[1])
    end

    # The location of the Stage on the screen.
    #
    #   x, y = stage.location
    #
    def location
      return @frame.location.x, @frame.location.y
    end

    # Sets the location of the Stage on the screen.
    #
    #   stage.location = [500, 200]
    #
    def location= values
      @frame.set_location(values[0], values[1])
    end

    # Opens the Stage and loads the provided Scene.
    #
    # See load_scene
    #
    def open(scene)
      scene.illuminate
      load_scene(scene)
      @frame.open
      scene.visible = true
      scene.scene_opened(self)
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
      @frame.setJMenuBar(scene.menu_bar)
      @frame.load(scene.panel)
      scene.stage = self
#      scene.panel.snap_to_size  # What's this for?
      if(has_static_size?(scene.style))
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
      frame.alert(message)
    end

    private ###############################################
    
    def build_frame
      @frame = UI::Model::Frame.new(self)
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
    
  end
    
end

