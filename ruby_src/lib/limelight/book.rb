require 'limelight/java_util'
require 'limelight/menu_bar'
require 'limelight/loaders/file_scene_loader'

module Limelight
  
  class Book
    attr_accessor :directory, :default_scene, :styles
    attr_reader :frame, :current_scene, :producer
    
    def public_choose_file
      choose_file
    end
    
    def initialize(producer)
      @producer = producer
      @styles = {}
      @frame = javax.swing.JFrame.new
      @frame.setDefaultCloseOperation(javax.swing.WindowConstants::EXIT_ON_CLOSE)
      @frame.setLayout(nil)
      @frame.setSize(900, 900)
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
      @frame.setVisible(true)
      @frame.repaint
      scene.visible = true
    end
  
    def close
      @frame.setVisible(false)
      @frame.dispose()
    end
    
    def loadScene(scene)
      @frame.content_pane.removeAll
      @frame.add(scene.panel)
      scene.book = self
      scene.panel.size = @frame.size
      @current_scene = scene
    end
    
    def load(scene_path)
      @producer.open_scene(scene_path)
    end
  
    def reload
      load(@current_scene.loader.scene_file)
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

