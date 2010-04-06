#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module PackageServer
    class Package
      
      attr_accessor :contents, :file_name
      
      def self.find_by_name(name, path)
        package = Package.new
        
        package_directory = "#{path}/#{name}"
        file_name = "#{name}.llp"
        
        if File.exists?(package_directory)
          File.delete(file_name) if File.exists?(file_name)
          Kernel.system("jruby -S limelight pack #{package_directory}")
          
          if File.exists?(file_name)
            package.file_name = file_name
            package.contents = IO.read(file_name)
          end
        end
        
        return package
      end
      
    end
  end
end