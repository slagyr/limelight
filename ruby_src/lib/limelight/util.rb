module Limelight
  
  module Util
    
    def self.is_limelight_scene?(file)
      return file.isDirectory() && File.exists?(File.join(file.absolute_path, "props.rb"))
    end
    
    def self.is_limelight_theater?(file)
      return file.isDirectory() && File.exists?(File.join(file.absolute_path, "index.rb"))
    end
    
  end
  
end