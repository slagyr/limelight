module Limelight
  module Loaders
    
    class FileLoader
    
      attr_reader :page_file, :root, :current_dir
    
      def initialize(filename)
        @page_file = File.expand_path(filename)
        @root = ""
        @current_dir = File.dirname(@page_file)
      end
      
      def path_to(path)
        if(path[0] == "/"[0])
          return path
        else
          return File.expand_path(File.join(@current_dir, path))
        end
      end
      
      def load(path)
puts "path: #{path}"        
        file_to_load = path_to(path)
puts "file_to_load: #{file_to_load}"        
        return IO.read(file_to_load)
      end
      
    end
    
  end
end