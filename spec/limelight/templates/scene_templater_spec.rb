#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../spec_helper")
require 'limelight/templates/scene_templater'

describe Limelight::Templates::SceneTemplater do

  it "should initialize" do
    templater = Limelight::Templates::SceneTemplater.new(:production_path => "prod", :scene_path => "some_scene", :spec_path => "spec")

    templater.target_root.should == "./prod"
    templater.source_root.should == Limelight::Templates::Templater.source_dir
    templater.tokens[:SCENE_NAME].should == "some_scene"
    templater.tokens[:SCENE_TITLE].should == "Some Scene"
  end

  it "should generate files" do
    templater = Limelight::Templates::SceneTemplater.new(:production_path => "prod", :scene_path => "some_scene", :spec_path => "spec")

    templater.should_receive(:file).with("some_scene/props.rb", "scene/props.rb.template", templater.tokens)
    templater.should_receive(:file).with("some_scene/styles.rb", "scene/styles.rb.template", templater.tokens)
    templater.should_receive(:directory).with("some_scene/players")
    templater.should_receive(:file).with("spec/some_scene/some_scene_spec.rb", "scene_spec/scene_spec.rb.template", templater.tokens)

    templater.generate
  end

  it "should generate files for a nested scene" do
    templater = Limelight::Templates::SceneTemplater.new(:production_path => "prod", :scene_path => "nested/some_scene", :spec_path => "spec")

    templater.should_receive(:file).with("nested/some_scene/props.rb", "scene/props.rb.template", templater.tokens)
    templater.should_receive(:file).with("nested/some_scene/styles.rb", "scene/styles.rb.template", templater.tokens)
    templater.should_receive(:directory).with("nested/some_scene/players")
    templater.should_receive(:file).with("spec/nested/some_scene/some_scene_spec.rb", "scene_spec/scene_spec.rb.template", templater.tokens)

    templater.generate
  end

  it "should generate files" do
    templater = Limelight::Templates::SceneTemplater.new(:production_path => "prod", :scene_path => "some_scene", :spec_path => "../spec")

    templater.should_receive(:file).with("some_scene/props.rb", "scene/props.rb.template", templater.tokens)
    templater.should_receive(:file).with("some_scene/styles.rb", "scene/styles.rb.template", templater.tokens)
    templater.should_receive(:directory).with("some_scene/players")
    templater.should_receive(:file).with("../spec/some_scene/some_scene_spec.rb", "scene_spec/scene_spec.rb.template", templater.tokens)

    templater.generate
  end

end