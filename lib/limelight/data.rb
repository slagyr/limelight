module Limelight

  class Data

    class << self

      def reset
        @root = nil
      end

      def root
        @root = calculate_root if @root == nil
        return @root
      end

      def downloads_dir
        return File.join(root, "Downloads")    
      end

      def productions_dir
        return File.join(root, "Productions")    
      end

      def establish_data_dirs
        Dir.mkdir(root) if !File.exists?(root)
        Dir.mkdir(downloads_dir) if !File.exists?(downloads_dir)
        Dir.mkdir(productions_dir) if !File.exists?(productions_dir)
      end

      private #############################################

      def calculate_root
        case Context.os
        when "osx"
          return File.expand_path(File.join("~/Library/Application Support/Limelight"))
        when "windows"
          return File.expand_path(File.join("~/Application Data/Limelight"))
        else
          raise "Unknown operating system: #{Context.os}"
        end
      end

    end

  end

end