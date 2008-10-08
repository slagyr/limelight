require 'limelight/commands/command'

module Limelight
  module Commands

    # Creates the directories and files for a production and/or scene.
    #
    #   jruby -S limelight create production <production_path>
    #
    class CreateCommand < Command

      install_as "create"

      def self.description
        return "Creates the directories and files for a production and/or scene."
      end

      protected ###########################################

      def process
        if @template_type == "production"
          create_production
        elsif @template_type == "scene"
          create_scene
        else
          raise "Unknown template type: #{@template_type}"
        end
      end

      def parameter_description
        return "[production|scene] <path>"
      end

      def parse_remainder(args)
        @template_type = args.shift
        raise "Missing template type" if @template_type.nil?
        @path = args.shift
        raise "Missing path parameter" if @path.nil?
      end

      def do_requires
        require 'limelight/templates/production_templater'
        require 'limelight/templates/scene_templater'
      end

      private #############################################

      def create_production
        Templates::ProductionTemplater.new(@path, "default_scene").generate
        Templates::SceneTemplater.new("#{@path}/default_scene").generate
      end

      def create_scene
        Templates::SceneTemplater.new(@path).generate
      end

    end

  end
end