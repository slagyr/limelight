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
      def initialize(scene_path)
        super(File.dirname(scene_path), Templater.source_dir)
        @scene_path = File.basename(scene_path)
        scene_name = File.basename(scene_path)
        @tokens = {}
        @tokens[:SCENE_NAME] = scene_name
        @tokens[:SCENE_TITLE] = scene_name.titleized
      end

      # Generated the files
      #
      def generate
        file(File.join(@scene_path, "props.rb"), "scene/props.rb.template", @tokens)
        file(File.join(@scene_path, "styles.rb"), "scene/styles.rb.template", @tokens)
        directory(File.join(@scene_path, "players"))
      end

    end

  end
end