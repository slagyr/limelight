#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  
  class FileFilter < javax.swing.filechooser::FileFilter
    
    attr_reader :description, :filter
    
    def initialize(description, &filter)
      super()
      @description = description
      @filter = filter
    end
    
    def accept(file)
      return @filter.call(file)
    end
    
    def getDescription
      return @description
    end
    
  end
  
end