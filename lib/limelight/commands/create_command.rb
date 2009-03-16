#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/commands/command'

module Limelight
  module Commands

    # See the following usages summary
    #
    #     Usage: limelight create [options] <production|scene> <path>
    #        Creates the directories and files for a production and/or scene.
    #        options:
    #        -h, --help                       Prints this usage summary.
    #        -s, --scene=<name>               Name of scene when creating a production.  Defaults to 'default_scene'.
    #
    # Assume you wanted to create a new production named "love_story" containing two scenes, "midnight_romance" and
    # "happily_ever_after".  The following commands would created all the needed files.
    #
    #   $ jruby -S limelight create -s midnight_romance production love_story
    #   $ jruby -S limelight create scene love_story/happily_ever_after
    #
    class CreateCommand < Command

      install_as "create"

      def self.description #:nodoc:
        return "Creates the directories and files for a production and/or scene."
      end

      protected ###########################################

      attr_reader :template_type, :path, :default_scene_name  #:nodoc:

      def initialize #:nodoc:
        @default_scene_name = "default_scene"
        @actions = {}
        @actions["production"] = :create_production
        @actions["scene"] = :create_scene
      end

      def process #:nodoc:
        action = @actions[@template_type]
        self.send action
      end

      def parameter_description #:nodoc:
        return "[options] <production|scene> <path>"
      end

      def parse_remainder(args) #:nodoc:
        @template_type = args.shift
        raise "Missing template type" if @template_type.nil?
        raise "Unknown template type: #{@template_type}" if @actions[@template_type].nil?
        @path = args.shift
        raise "Missing path parameter" if @path.nil?
      end

      def do_requires #:nodoc:
        require 'limelight/templates/production_templater'
        require 'limelight/templates/scene_templater'
      end

      def build_options(spec) #:nodoc:
        spec.on("-s <name>", "--scene=<name>", "Name of scene when creating a production.  Defaults to 'default_scene'.") { |value| @default_scene_name = value}
      end

      private #############################################

      def create_production
        Templates::ProductionTemplater.new(@path, @default_scene_name).generate
        Templates::SceneTemplater.new("#{@path}/#{@default_scene_name}").generate
      end

      def create_scene
        Templates::SceneTemplater.new(@path).generate
      end

    end

  end
end