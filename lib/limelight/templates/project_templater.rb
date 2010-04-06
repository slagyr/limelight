#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/templates/templater'
require 'limelight/string'
require 'limelight/version'

module Limelight
  module Templates

    # A Templater that creates all the files and directories for a production.  When used with "hamlet" as
    # the production_path, the following will be the result.
    #
    #    creating directory:  ./hamlet
    #    creating directory:  ./hamlet/features
    #    creating directory:  ./hamlet/features/step_definitions
    #    creating file:       ./hamlet/features/step_definitions/limelight_steps.rb
    #    creating directory:  ./hamlet/features/support
    #    creating file:       ./hamlet/features/support/env.rb
    #    creating directory:  ./hamlet/production
    #    creating directory:  ./hamlet/production/default_scene
    #    creating file:       ./hamlet/production/default_scene/props.rb
    #    creating file:       ./hamlet/production/default_scene/styles.rb
    #    creating file:       ./hamlet/production/production.rb
    #    creating file:       ./hamlet/production/stages.rb
    #    creating file:       ./hamlet/production/styles.rb
    #    creating file:       ./hamlet/Rakefile
    #    creating directory:  ./hamlet/spec
    #    creating directory:  ./hamlet/spec/default_scene
    #    creating file:       ./hamlet/spec/default_scene/default_scene_spec.rb
    #    creating file:       ./hamlet/spec/spec_helper.rb
    class ProjectTemplater < Templater
      
      attr_reader :tokens
      
      def initialize(options)
        super(File.dirname(options[:production_path]), Templater.source_dir)
        @tokens = {}
        @scene_path = options[:scene_path]
        @tokens[:DEFAULT_SCENE_NAME] = @scene_path
        @tokens[:SCENE_NAME] = @scene_path
        @tokens[:SCENE_TITLE] = @scene_path.titleized
        @tokens[:PRODUCTION_NAME] = File.basename(options[:production_path]).titleized
        @tokens[:LLP_NAME] = File.basename(options[:production_path])
        @tokens[:CURRENT_VERSION] = Limelight::VERSION::STRING
        @project_path = File.basename(options[:production_path])
      end
      
      def generate
        file(File.join(@project_path, "features/step_definitions/limelight_steps.rb"), "features/step_definitions/limelight_steps.rb.template", @tokens)
        file(File.join(@project_path, "features/support/env.rb"), "features/support/env.rb.template", @tokens)
        file(File.join(@project_path, "production", @scene_path, "props.rb"), "scene/props.rb.template", @tokens)
        file(File.join(@project_path, "production", @scene_path, "styles.rb"), "scene/styles.rb.template", @tokens)
        file(File.join(@project_path, "production/production.rb"), "production/production.rb.template", @tokens)
        file(File.join(@project_path, "production/stages.rb"), "production/stages.rb.template", @tokens)
        file(File.join(@project_path, "production/styles.rb"), "production/styles.rb.template", @tokens)
        file(File.join(@project_path, "Rakefile"), "project/Rakefile.template", @tokens)
        file(File.join(@project_path, "spec", @scene_path, "#{@scene_path}_spec.rb"), "scene_spec/scene_spec.rb.template", @tokens)
        file(File.join(@project_path, "spec/spec_helper.rb"), "project/spec_helper.rb.template", @tokens)
      end
    end
  end
end