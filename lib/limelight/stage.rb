#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/java_util'
require 'limelight/menu_bar'
require 'limelight/loaders/file_scene_loader'
require 'limelight/file_chooser'
require 'limelight/util'

module Limelight
  
  class Stage
    attr_accessor :default_scene
    attr_reader :frame, :current_scene, :name, :theater
    
    include Rapi::Stage
    
    def public_choose_file
      choose_file
    end
    
    def initialize(theater, name="default")
      @theater = theater
      @name = name
      build_frame
      self.title = @name
    end
    
    def title
      return @frame.title
    end
    
    def title=(value)
      @frame.title = value
    end
    
    def size
      return @frame.width, @frame.height
    end
    
    def size=(values)
      @frame.set_size(values[0], values[1])
    end
    
    def location
      return @frame.location.x, @frame.location.y
    end
    
    def location= values
      @frame.set_location(values[0], values[1])
    end
    
    def open(scene)
      load_scene(scene)
      @frame.open
      scene.visible = true
      scene.scene_opened(self)
    end
  
    def close
      @frame.close
    end
    
    def load_scene(scene)
      @frame.setJMenuBar(scene.menu_bar)
      @frame.load(scene.panel)
      scene.stage = self
      scene.panel.set_size(scene.panel.get_preferred_size)
      if(scene.has_static_size?)
        @frame.set_size(scene.panel.get_size)
      end
      @current_scene = scene
    end
    
    def choose_file(options={}, &block)
      options[:parent] = @frame
      chooser = FileChooser.new(options, &block)
      return chooser.choose_file
    end

    def alert(message)
      frame.alert(message)
    end

    private ###############################################
    
    def build_frame
      @frame = UI::Frame.new(self)
      @frame.set_size(800, 800)
      @frame.set_location(200, 25)
      @frame.title = title
    end
    
  end
    
end

