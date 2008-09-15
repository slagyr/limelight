#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight

  module Util

    def self.is_limelight_scene?(file)
      return is_directory_containing_file?(file, "props.rb")
    end

    def self.is_limelight_production?(file)
      return is_directory_containing_file?(file, "stages.rb")
    end

    def self.is_directory_containing_file?(file, file_name)
      if file.is_a? String
        return File.directory?(file) && File.exists?(File.join(file, file_name))
      else
        return file.isDirectory() && File.exists?(File.join(file.absolute_path, file_name))
      end
    end

    def self.lobotomize(klass)
      klass.methods.each do |method_name|
        unless method_name[0..1] == "__" || method_name == "instance_eval"
          begin
            klass.instance_eval "undef_method :#{method_name}"
          rescue Exception => e
          end
        end
      end
    end

  end

end