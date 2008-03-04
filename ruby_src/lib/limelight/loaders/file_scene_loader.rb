module Limelight
  module Loaders
    
    class FileSceneLoader
      
      include Java::limelight.SceneLoader
    
      attr_reader :scene_file, :current_dir
      
      def self.for_root(root)
        loader = new
        loader.reset_on_root(root)
        return loader
      end
      
      def self.for_scene(filename)
        loader = new
        loader.reset_on_scene(filename)
        return loader
      end
      
      def reset_on_root(root)
        @current_dir = File.expand_path(root)
      end
      
      def reset_on_scene(filename)
        @scene_file = File.expand_path(filename)
        @current_dir = File.dirname(@scene_file)
      end
      
      def path_to(path)
        if(path[0] == "/"[0])
          return path
        else
          return File.expand_path(File.join(@current_dir, path))
        end
      end
      
      def exists?(path)
        return File.exists?(path_to(path))
      end
      
      alias :pathTo :path_to
      
      def load(path)       
        file_to_load = path_to(path)        
        return IO.read(file_to_load)
      end
      
      alias :read :load
      
    end
    
  end
end