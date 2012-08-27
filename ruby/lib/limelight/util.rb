#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

module Limelight

  # Utility methods for Limelight
  #
  module Util

    # Returns true if the specified file is a directory and has the structure of a Scene.
    #
    def self.is_limelight_scene?(dir)
      return directory_contains_file(dir, "props.rb")
    end

    # Returns true if the specified file is a directory and has the structure of a Production.
    def self.is_limelight_production?(dir)
      return directory_contains_file(dir, "stages.rb")
    end

    # Returns true of the file is a directory containing an entry named file_name.
    #
    def self.directory_contains_file(file, file_name)
      fs = Java::limelight.Context.fs
      dir_path = file.is_a? String ? file : file.path
      return fs.directory?(dir_path) && fs.exists?(fs.join(dir_path, file_name))
    end

    # Removed all methods from a class except instance_eval and methods starting with __.
    # This is used by the DSL Builder classes to minimize reserved keywords.
    #
    def self.lobotomize(klass)
      klass.methods.each do |method|
        unless method.to_s()[0..1] == "__" || method == :instance_eval || method == :methods
          begin
            klass.instance_eval "undef_method :#{method}"
          rescue Exception => e
          end
        end
      end
    end

  end

end

