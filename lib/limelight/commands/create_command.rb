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

      def run(args)
        do_requires
        begin
          template_type = args.shift
          if template_type == "production"
            create_production(args)
          elsif template_type == "scene"
            create_scene(args)
          else
            raise "Unknown template type: #{template_type}"
          end
        rescue Exception => e
          usage(e)
        end
      end

      private #############################################

      def usage(exception = nil)
        puts exception if exception
        puts "Usage: limelight create [production|scene] <path>"
        exit -1
      end

      def create_production(args)
        production_path = args.shift
        raise "Missing path parameter" if production_path.nil?
        Templates::ProductionTemplater.new(production_path, "default_scene").generate
        Templates::SceneTemplater.new("#{production_path}/default_scene").generate
      end

      def create_scene(args)
        scene_path = args.shift
        raise "Missing path parameter" if scene_path.nil?
        Templates::SceneTemplater.new(scene_path).generate
      end

      def do_requires
        require 'limelight/templates/production_templater'
        require 'limelight/templates/scene_templater'
      end

    end

  end
end