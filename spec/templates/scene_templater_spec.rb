#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/templates/scene_templater'

describe Limelight::Templates::SceneTemplater do

  it "should initialize" do
    templater = Limelight::Templates::SceneTemplater.new("prod/some_scene")

    templater.target_root.should == "./prod"
    templater.source_root.should == Limelight::Templates::Templater.source_dir
    templater.tokens[:SCENE_NAME].should == "some_scene"
    templater.tokens[:SCENE_TITLE].should == "Some Scene"
  end

  it "should generate files" do
    templater = Limelight::Templates::SceneTemplater.new("prod/some_scene")

    templater.should_receive(:file).with("some_scene/props.rb", "scene/props.rb.template", templater.tokens)
    templater.should_receive(:file).with("some_scene/styles.rb", "scene/styles.rb.template", templater.tokens)
    templater.should_receive(:directory).with("some_scene/players")

    templater.generate
  end

end