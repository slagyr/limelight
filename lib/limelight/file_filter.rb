#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight

  # A FileFiler is used in conjunction with FileChooser.  It is used to help the user select only files of the
  # desired type.
  #
  class FileFilter < javax.swing.filechooser::FileFilter
    
    attr_reader :description, :filter

    # The filter parameter is a block that contains the logic to decide whether a given file is selectable or not.
    #
    def initialize(description, &filter)
      super()
      @description = description
      @filter = filter
    end

    # Called to determine if a file is selectable.  Invokes the filter block.
    #
    def accept(file)
      return @filter.call(file)
    end
    
    def getDescription
      return @description
    end
    
  end
  
end