require 'limelight/file_filter'

module Limelight
  
  class FileChooser
    
    attr_reader :chooser, :chosen_file
    
    def initialize(options = {}, &filter)
      @options = options
      @parent = options[:parent]
      create_chooser
      @chooser.setFileFilter(FileFilter.new(@options[:description], &filter)) if filter
    end
    
    def choose_file
      returnVal = @chooser.showOpenDialog(@parent);
      
      if(returnVal == javax.swing.JFileChooser::APPROVE_OPTION)
        @chosen_file = @chooser.getSelectedFile().absolute_path
      else
        @chosen_file = nil
      end
      
      return @chosen_file
    end
    
    private ###############################################
    
    def create_chooser
      if @options.has_key?(:directory)
        @chooser = javax.swing.JFileChooser.new(@options[:directory])
      else
        @chooser = javax.swing.JFileChooser.new
      end
      configure_file_selection_mode
      @chooser.setDialogTitle(@options[:title]) if @options[:title]
    end
    
    def configure_file_selection_mode
      if(@options[:directories_only])
        @chooser.setFileSelectionMode(javax.swing.JFileChooser::DIRECTORIES_ONLY)
      elsif(@options[:files_only])
        @chooser.setFileSelectionMode(javax.swing.JFileChooser::FILES_ONLY)
      else
        @chooser.setFileSelectionMode(javax.swing.JFileChooser::FILES_AND_DIRECTORIES)
      end
    end
    
  end
  
end