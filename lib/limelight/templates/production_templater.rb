#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/templates/templater'
require 'limelight/string'
require 'limelight/version'

module Limelight
  module Templates

    # A Templater that creates all the files and directories for a production.  When used with "love_story" as
    # the production_path, the following will be the result.
    #
    #    creating directory:  ./love_story
    #    creating file:       ./love_story/production.rb
    #    creating file:       ./love_story/stages.rb
    #    creating file:       ./love_story/styles.rb
    #
    class ProductionTemplater < Templater

      attr_reader :tokens

      # To create a ProductionTemplater, provide a production_path and the name of the default scene.
      #
      def initialize(options)
        production_path = options[:production_path]
        default_scene_name = options[:scene_path]
        super(File.dirname(production_path), Templater.source_dir)
        @production_path = File.basename(production_path)
        @spec_path = File.join(@production_path, options[:spec_path])
        @tokens = {}
        @tokens[:DEFAULT_SCENE_NAME] = default_scene_name
        @tokens[:PRODUCTION_NAME] = File.basename(production_path).titleized
        @tokens[:CURRENT_VERSION] = Limelight::VERSION::STRING
      end

      # Generates the files
      #
      def generate
        file(File.join(@production_path, "production.rb"), "production/production.rb.template", @tokens)
        file(File.join(@production_path, "stages.rb"), "production/stages.rb.template", @tokens)
        file(File.join(@production_path, "styles.rb"), "production/styles.rb.template", @tokens)
        file(File.join(@spec_path, "spec_helper.rb"), "production/spec/spec_helper.rb.template", @tokens)
      end

    end

  end
end