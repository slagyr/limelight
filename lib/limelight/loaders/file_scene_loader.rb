#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Loaders
    
    class FileSceneLoader
      
      include SceneLoader
    
      attr_reader :root
      
      def self.for_root(root)
        loader = new
        loader.reset_on_root(root)
        return loader
      end
      
      def reset_on_root(root)
        @root = File.expand_path(root)
      end
      
      def path_to(path)
        #TODO What about windoze?
        if(path[0] == "/"[0])
          return path
        else
          return File.expand_path(File.join(@root, path))
        end
      end
      
      def exists?(path)
        return File.exists?(path_to(path))
      end
      
      alias :pathTo :path_to
      
      def load(path)       
        file_to_load = path_to(path)
        raise LimelightException.new("File not found: #{file_to_load}") if not File.exists?(file_to_load)  
        return IO.read(file_to_load)
      end
      
      alias :read :load
      
    end
    
  end
end