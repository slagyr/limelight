#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/templates/templater'
require 'limelight/string'

module Limelight
  module Templates

    # A derivative of Templater that generates all the files for a scene.
    # When created with the path "midnight_romance" the following will result:
    #
    #    creating directory:  ./midnight_romance
    #    creating file:       ./midnight_romance/props.rb
    #    creating file:       ./midnight_romance/styles.rb
    #    creating directory:  ./midnight_romance/players
    #
    class SceneTemplater < Templater

      attr_reader :tokens

      # The scene_path should be path to a desired scene inside a production directory.
      #
      def initialize(options)
        @production_path = options[:production_path]
        @scene_path = options[:scene_path]
        @spec_path = options[:spec_path]
        super(@production_path, Templater.source_dir)
        @scene_name = File.basename(@scene_path)
        @tokens = {}
        @tokens[:SCENE_NAME] = @scene_name
        @tokens[:SCENE_TITLE] = @scene_name.titleized
      end

      # Generates the files
      #
      def generate
        file(File.join(@scene_path, "props.rb"), "scene/props.rb.template", @tokens)
        file(File.join(@scene_path, "styles.rb"), "scene/styles.rb.template", @tokens)
        directory(File.join(@scene_path, "players"))
        file(File.join(@spec_path, @scene_path, "#{@scene_name}_spec.rb"), "scene_spec/scene_spec.rb.template", @tokens)
      end

    end

  end
end