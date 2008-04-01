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