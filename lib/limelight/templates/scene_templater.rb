require 'limelight/templates/templater'
require 'limelight/string'

module Limelight
  module Templates

    class SceneTemplater < Templater

      attr_reader :tokens

      def initialize(scene_path)
        super(File.dirname(scene_path), Templater.source_dir)
        @scene_path = File.basename(scene_path)
        scene_name = File.basename(scene_path)
        @tokens = {}
        @tokens[:SCENE_NAME] = scene_name
        @tokens[:SCENE_TITLE] = scene_name.titleized
      end

      def generate
        file(File.join(@scene_path, "props.rb"), "scene/props.rb.template", @tokens)
        file(File.join(@scene_path, "styles.rb"), "scene/styles.rb.template", @tokens)
        directory(File.join(@scene_path, "players"))
      end

    end

  end
end