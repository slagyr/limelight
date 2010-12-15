#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/production'

describe Limelight::Production, "Instance methods" do

  before(:each) do
    @fs = Java::limelight.io.FakeFileSystem.installed
    @studio = Limelight::Studio.install
    @peer = Java::limelight.ruby.RubyProduction.new("/test_prod")
    @production = Limelight::Production.new(@peer)
  end

  it "should know its stages file" do
    @production.stages_file.should == "/test_prod/stages.rb"
  end

  it "should know its styles file" do
    @production.styles_file.should == "/test_prod/styles.rb"
  end

  it "should know its gems directory" do
    @production.gems_directory.should == "/test_prod/__resources/gems/gems"
  end

  it "should know its gems root" do
    @production.gems_root.should == "/test_prod/__resources/gems"
  end

  it "should provide paths to it's scenes" do
    @production.scene_directory("one").should == "/test_prod/one"
    @production.scene_directory("two").should == "/test_prod/two"
  end

  it "should allow close by default" do
    @production.allow_close?.should == true
  end

  it "should tell producer to do the closing" do
    @peer.should_receive(:close)

    @production.close
  end

  it "extends the production if production.rb is present" do
    @fs.create_text_file("/test_prod/production.rb", "self.name = \"Fido\"; def foo; end;")

    @production.illuminate

    @production.name.should == "Fido"
    @production.respond_to?(:foo).should == true
  end

  it "loads stages" do
    @fs.create_text_file("/test_prod/stages.rb", "stage 'FrontStage' do; end;")

    @production.load_stages

    @production.theater["FrontStage"].should_not == nil
  end

  it "loads a scene from props.rb" do
    @fs.create_text_file("/test_prod/scene/props.rb", "parent do; child; end;")

    scene = @production.load_scene("scene")

    scene.should_not == nil
    scene.children.size.should == 1
    scene.children[0].children.size.should == 1
  end

  it "loads styles" do
    @fs.create_text_file("/test_prod/scene/styles.rb", "cool { width 100; x 42 }")

    styles = @production.load_styles("test_prod/scene", {})

    styles["cool"].should_not == nil
    styles["cool"].width.should == "100"
    styles["cool"].x.should == "42"
  end

end


