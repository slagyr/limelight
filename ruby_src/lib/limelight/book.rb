require 'limelight/limelight_java'
require 'limelight/llm_parser'

module Limelight
  
  class ChooseFileFilter < javax.swing.filechooser::FileFilter
    
    def accept(file)
      return file.name[-3..-1] == "llm"
    end
    
    def getDescription
      return "Limelight Markup File"
    end
  end
  
  class OpenActionListener
    include java.awt.event.ActionListener
    
    def initialize(book)
      @book = book
    end
    
    def actionPerformed(event)
      chooser = javax.swing.JFileChooser.new
      chooser.setCurrentDirectory(@book.directory) if @book.directory
      chooser.setFileFilter(ChooseFileFilter.new)
      returnVal = chooser.showOpenDialog(@book.frame);
      
      if(returnVal == javax.swing.JFileChooser::APPROVE_OPTION)
        @book.load(chooser.getSelectedFile().getAbsolutePath());
        @book.directory = chooser.getSelectedFile().getAbsoluteFile().getParentFile();
      end
    end
  end
  
  class RefreshActionListener
    include java.awt.event.ActionListener
    
    def initialize(book)
      @book = book
    end
    
    def actionPerformed(event)
      @book.reload
    end
  end
  
  class Book
    attr_accessor :directory
    attr_reader :frame
    
    def initialize
      @frame = javax.swing.JFrame.new
        
      menuBar = javax.swing.JMenuBar.new
      frame.setJMenuBar(menuBar)
      fileMenu = javax.swing.JMenu.new("File")
      menuBar.add(fileMenu)
      
      open = javax.swing.JMenuItem.new("Open")
      fileMenu.add(open)
      open.addActionListener(OpenActionListener.new(self))
      
      refresh = javax.swing.JMenuItem.new("Refresh")
      fileMenu.add(refresh);
      refresh.addActionListener(RefreshActionListener.new(self))
    end
    
     def open(page)
      @frame.setLayout(nil)
      @frame.setSize(900, 900)
      @frame.setLocation(200, 25)
      @frame.setDefaultCloseOperation(javax.swing.WindowConstants::EXIT_ON_CLOSE)
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
      @frame.add(page.panel)
      page.book = self
      page.panel.size = @frame.size
    end
    
    def load(llm_file)
      @current_file = llm_file
      parser = LlmParser.new
      dir = File.expand_path(File.dirname(llm_file))
      Dir.chdir(dir)
      $: << dir
      page = parser.parse(IO.read(File.basename(llm_file)))
      open(page)
    end
  
    def reload
      load(@current_file)
    end

  end
    
end

