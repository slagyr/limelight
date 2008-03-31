require 'limelight/java_util'
require 'limelight/menu_bar'
require 'limelight/loaders/file_scene_loader'

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
      @producer.open_scene(scene_path)
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
          item("Open", :choose_file)
          item("Refresh", :reload)
        end
      end
      
      @frame.setJMenuBar(menu_bar)
    end
    
    def choose_file
      chooser = javax.swing.JFileChooser.new
      chooser.setCurrentDirectory(@directory) if @directory
      chooser.setFileFilter(ChooseFileFilter.new)
      returnVal = chooser.showOpenDialog(@frame);
      
      if(returnVal == javax.swing.JFileChooser::APPROVE_OPTION)
        load(chooser.getSelectedFile().getAbsolutePath());
        @directory = chooser.getSelectedFile().getAbsoluteFile().getParentFile();
      end
    end

  end
  
  class ChooseFileFilter < javax.swing.filechooser::FileFilter
    
    def accept(file)
      return file.name[-3..-1] == "llm"
    end
    
    def getDescription
      return "Limelight Markup File"
    end
  end
    
end

