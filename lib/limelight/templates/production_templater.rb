require 'limelight/templates/templater'
require 'limelight/string'

module Limelight
  module Templates

    class ProductionTemplater < Templater

      attr_reader :tokens

      def initialize(production_path, default_scene_name)
        super(File.dirname(production_path), Templater.source_dir)
        @production_path = File.basename(production_path)
        @tokens = {}
        @tokens[:DEFAULT_SCENE_NAME] = default_scene_name
        @tokens[:PRODUCTION_NAME] = File.basename(production_path).titleized
      end

      def generate
        file(File.join(@production_path, "production.rb"), "production.rb.template", @tokens)
        file(File.join(@production_path, "init.rb"), "init.rb.template", @tokens)
        file(File.join(@production_path, "stages.rb"), "stages.rb.template", @tokens)
        file(File.join(@production_path, "styles.rb"), "styles.rb.template", @tokens)
      end

    end

  end
end