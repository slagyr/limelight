#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/file_filter'

module Limelight

  # An object that manages the selection of a file on the file system.  When invoked, a file chooser window
  # will pop up and allow the user to select a file.
  #
  class FileChooser
    
    attr_reader :chooser, :chosen_file

    # Creates a new FileChooser. Options may include:
    # * :description => a string describing the desired file
    # * :parent => (required) the parent window
    # * :directory => starting directory
    # * :title => title of the window
    # * :directories_only => boolean, true if the user must select a directory
    # * :files_only => boolean, true if the user must select a file
    #
    def initialize(options = {}, &filter)
      @options = options
      @parent = options[:parent]
      create_chooser
      @chooser.setFileFilter(FileFilter.new(@options[:description], &filter)) if filter
    end

    # Opens the windows and returns the chosen file.
    #
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