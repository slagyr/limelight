require 'limelight/java_util'
require 'limelight/menu_bar'
require 'limelight/loaders/file_scene_loader'

module Limelight
  
  class Stage
    attr_accessor :directory, :default_scene, :styles
    attr_reader :frame, :current_scene, :producer
    
    include Java::limelight.ui.Stage
    
    def public_choose_file
      choose_file
    end
    
    def initialize(producer)
      @producer = producer
      @styles = {}
      @frame = Java::limelight.ui.Frame.new(self)
      @frame.setLocation(200, 25)
      
      menu_bar = MenuBar.build(self) do
        menu("File") do
          item("Open", :choose_file)
          item("Refresh", :reload)
        end
      end
      
      frame.setJMenuBar(menu_bar)
    end
    
    def open(scene)
      loadScene(scene)
      @frame.open
      scene.visible = true
    end
  
    def close
      @frame.close
    end
    
    def loadScene(scene)
      @frame.load(scene.panel)
      scene.stage = self
      scene.panel.set_size(scene.panel.get_preferred_size)
      if(scene.has_static_size?)
        @frame.set_size(scene.panel.get_size)
      else
        @frame.set_size(800, 800) if @frame.get_width == 0 || @frame.get_height == 0
      end
      @current_scene = scene
    end
    
    def load(scene_path)
      @producer.open_scene(scene_path)
    end
  
    def reload
      load(@current_scene.path)
    end
    
    def resized
      @current_scene.update
    end

    private ###############################################
    
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

