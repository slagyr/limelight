require 'limelight/java_util'
require 'limelight/menu_bar'
require 'limelight/loaders/file_scene_loader'
require 'limelight/file_chooser'
require 'limelight/util'

module Limelight
  
  class Stage
    attr_accessor :directory, :default_scene, :styles
    attr_reader :frame, :current_scene, :producer, :name
    
    include Java::limelight.ui.Stage
    
    def public_choose_file
      choose_file
    end
    
    def initialize(producer, name="default")
      @producer = producer
      @name = name
      @styles = {}
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
    end
  
    def close
      @frame.close
    end
    
    def load_scene(scene)
      @frame.load(scene.panel)
      scene.stage = self
      scene.panel.set_size(scene.panel.get_preferred_size)
      if(scene.has_static_size?)
        @frame.set_size(scene.panel.get_size)
      end
      @current_scene = scene
    end
    
    def load(scene_path)
      @producer.open_scene(scene_path, self)
    end
  
    def reload
      load(@current_scene.path)
    end

    private ###############################################
    
    def build_frame
      @frame = Java::limelight.ui.Frame.new(self)
      @frame.set_size(800, 800)
      @frame.set_location(200, 25)
      @frame.title = title
      
      menu_bar = MenuBar.build(self) do
        menu("File") do
          item("Open", :open_chosen_scene)
          item("Refresh", :reload)
        end
      end
      
      @frame.setJMenuBar(menu_bar)
    end
    
    def open_chosen_scene
      chooser = FileChooser.new(:parent => @frame, :title => "Open New Limelight Scene", :description => "Limelight Scene", :directory => @directory) { |file| Util.is_limelight_scene?(file) || Util.is_limelight_theater?(file) }
      if(chooser.choose_file)
        load(chooser.chosen_file)
        @directory = File.dirname(chooser.chosen_file)
      end
    end
    
  end
    
end

