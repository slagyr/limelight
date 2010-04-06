#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../spec_helper")
require 'limelight/templates/project_templater'

describe Limelight::Templates::ProjectTemplater do
  it "should initialize settings" do
    templater = Limelight::Templates::ProjectTemplater.new(:production_path => "hamlet", :scene_path => "default_scene")
  
    templater.target_root.should == "."
    templater.source_root.should == Limelight::Templates::Templater.source_dir
    templater.tokens[:PRODUCTION_NAME].should == "Hamlet"
    templater.tokens[:DEFAULT_SCENE_NAME].should == "default_scene"
    templater.tokens[:CURRENT_VERSION].should == Limelight::VERSION::STRING
    templater.tokens[:LLP_NAME].should == "hamlet"
  end
  
  it "should set the scene token options" do
    templater = Limelight::Templates::ProjectTemplater.new(:production_path => "hamlet", :scene_path => "default_scene")
  
    templater.tokens[:DEFAULT_SCENE_NAME].should == "default_scene"
    templater.tokens[:SCENE_NAME].should == "default_scene"
    templater.tokens[:SCENE_TITLE].should == "Default Scene"
  end

  it "should generate files" do
    templater = Limelight::Templates::ProjectTemplater.new(:production_path => "hamlet", :scene_path => "default_scene")

    templater.should_receive(:file).with("hamlet/features/step_definitions/limelight_steps.rb", "features/step_definitions/limelight_steps.rb.template", templater.tokens)
    templater.should_receive(:file).with("hamlet/features/support/env.rb", "features/support/env.rb.template", templater.tokens)
    templater.should_receive(:file).with("hamlet/production/default_scene/props.rb", "scene/props.rb.template", templater.tokens)
    templater.should_receive(:file).with("hamlet/production/default_scene/styles.rb", "scene/styles.rb.template", templater.tokens)
    templater.should_receive(:file).with("hamlet/production/production.rb", "production/production.rb.template", templater.tokens)
    templater.should_receive(:file).with("hamlet/production/stages.rb", "production/stages.rb.template", templater.tokens)
    templater.should_receive(:file).with("hamlet/production/styles.rb", "production/styles.rb.template", templater.tokens)
    templater.should_receive(:file).with("hamlet/Rakefile", "project/Rakefile.template", templater.tokens)
    templater.should_receive(:file).with("hamlet/spec/default_scene/default_scene_spec.rb", "scene_spec/scene_spec.rb.template", templater.tokens)
    templater.should_receive(:file).with("hamlet/spec/spec_helper.rb", "project/spec_helper.rb.template", templater.tokens)

    templater.generate
  end
  
  it "should allow the default scene name to be specified" do
    templater = Limelight::Templates::ProjectTemplater.new(:production_path => "hamlet", :scene_path => "wall")
    templater.tokens[:DEFAULT_SCENE_NAME].should == "wall"

    templater.should_receive(:file).with("hamlet/features/step_definitions/limelight_steps.rb", "features/step_definitions/limelight_steps.rb.template", templater.tokens)
    templater.should_receive(:file).with("hamlet/features/support/env.rb", "features/support/env.rb.template", templater.tokens)
    templater.should_receive(:file).with("hamlet/production/wall/props.rb", "scene/props.rb.template", templater.tokens)
    templater.should_receive(:file).with("hamlet/production/wall/styles.rb", "scene/styles.rb.template", templater.tokens)
    templater.should_receive(:file).with("hamlet/production/production.rb", "production/production.rb.template", templater.tokens)
    templater.should_receive(:file).with("hamlet/production/stages.rb", "production/stages.rb.template", templater.tokens)
    templater.should_receive(:file).with("hamlet/production/styles.rb", "production/styles.rb.template", templater.tokens)
    templater.should_receive(:file).with("hamlet/Rakefile", "project/Rakefile.template", templater.tokens)
    templater.should_receive(:file).with("hamlet/spec/wall/wall_spec.rb", "scene_spec/scene_spec.rb.template", templater.tokens)
    templater.should_receive(:file).with("hamlet/spec/spec_helper.rb", "project/spec_helper.rb.template", templater.tokens)

    templater.generate
  end
  
  it "should create a nested project" do
    templater = Limelight::Templates::ProjectTemplater.new(:production_path => "shakespeare/hamlet", :scene_path => "default_scene")

    templater.target_root.should == "./shakespeare"
  end
end