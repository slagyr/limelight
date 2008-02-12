require 'limelight/java_util'
require 'limelight/llm_parser'
require 'limelight/menu_bar'
require 'limelight/loaders/file_page_loader'

module Limelight
  
  class Book
    attr_accessor :directory
    attr_reader :frame, :current_page
    
    def public_choose_file
      choose_file
    end
    
    def initialize
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
    
    def open(page)
      loadPage(page)
      @frame.setVisible(true)
      @frame.repaint
    end
  
    def close
      @frame.setVisible(false)
      @frame.dispose()
    end
    
    def loadPage(page)
      @frame.content_pane.removeAll
      @frame.add(page.frame)
      page.book = self
      @current_page = page
    end
    
    def load(llm_file)
      if(@current_page)
        loader = Loaders::FilePageLoader.new(@current_page.loader.path_to(llm_file))
      else
        loader = Loaders::FilePageLoader.new(llm_file)
      end 
      parser = LlmParser.new
      page_content = loader.load(loader.page_file) 
      page = parser.parse(page_content, loader)
      open(page)
    end
  
    def reload
      load(@current_page.loader.page_file)
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

