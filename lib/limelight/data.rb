#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight

  class Data

    class << self

      def reset
        @root = nil
      end

      def root
        @root = Context.instance.os.dataRoot if @root == nil
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

    end

  end

end