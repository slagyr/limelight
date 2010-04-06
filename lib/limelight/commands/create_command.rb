#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/commands/command'

module Limelight
  module Commands

    # See the following usages summary
    #
    #  Usage: limelight create [options] <production|scene> <path>
    #      Creates the directories and files for a production and/or scene.
    #      options:
    #      -h, --help                       Prints this usage summary.
    #      -s, --scene=<name>               Name of scene when creating a production.  Defaults to 'default_scene'.
    #      -p, --production_path=<path>     Path of production to contain scene.  Defaults to '.'.
    #      -S, --spec_path=<path>           Path to spec directory, relative to <production_path>.  Defaults to 'spec'.
    #
    # Assume you wanted to create a new production named "love_story" containing two scenes, "midnight_romance" and
    # "happily_ever_after".  The following commands would created all the needed files.
    #
    #   $ jruby -S limelight create -s midnight_romance production love_story
    #   $ jruby -S limelight create -p love_story scene happily_ever_after
    #
    class CreateCommand < Command

      install_as "create"

      def self.description #:nodoc:
        return "Creates the directories and files for a production and/or scene."
      end

      attr_reader :template_type, :scene_name, :production_path, :spec_path  #:nodoc:

      protected ###########################################

      def initialize #:nodoc:
        @scene_name = "default_scene"
        @production_path = "."
      end

      def process #:nodoc:
        create_method = "create_#{@template_type}".to_s
        self.send create_method
      end

      def parameter_description #:nodoc:
        return "[options] <production|scene|project> <path>"
      end

      def parse_remainder(args) #:nodoc:
        @template_type = args.shift
        raise "Missing template type" if @template_type.nil?
        raise "Unknown template type: #{@template_type}" if !["production", "scene", "project"].include?(@template_type)
        self.send "parse_#{@template_type}".to_sym, args
        @spec_path = "spec" unless @spec_path
      end

      def do_requires #:nodoc:
        require 'limelight/templates/production_templater'
        require 'limelight/templates/scene_templater'
        require 'limelight/templates/project_templater'
      end

      def build_options(spec) #:nodoc:
        spec.on("-s <name>", "--scene=<name>", "Name of scene when creating a production.  Defaults to 'default_scene'.") { |value| @scene_name = value}
        spec.on("-p <path>", "--production_path=<path>", "Path of production to contain scene.  Defaults to '.'.") { |value| @production_path = value}
        spec.on("-S <path>", "--spec_path=<path>", "Path to spec directory, relative to <production_path>.  Defaults to 'spec'.") { |value| @spec_path = value}
      end

      private #############################################

      def parse_project(args)
        @production_path = args.shift
        raise "Missing project path parameter" if @production_path.nil?
      end
      
      def parse_production(args)
        @production_path = args.shift
        raise "Missing production path parameter" if @production_path.nil?
      end

      def parse_scene(args)
        @scene_name = args.shift
        raise "Missing scene name/path parameter" if @scene_name.nil?
      end

      def create_project
        Templates::ProjectTemplater.new(options_hash).generate
      end

      def create_production
        Templates::ProductionTemplater.new(options_hash).generate
        Templates::SceneTemplater.new(options_hash).generate
      end

      def create_scene
        Templates::SceneTemplater.new(options_hash).generate
      end

      def options_hash
        return {:production_path => @production_path, :scene_path => @scene_name, :spec_path => @spec_path}
      end

    end

  end
end