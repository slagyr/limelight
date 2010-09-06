#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../../spec_helper")
require 'limelight/builtin/players'
require 'limelight/prop'

describe Limelight::Builtin::Players::Image do

  before(:each) do
    @prop = Limelight::Prop.new
    Limelight::Player.cast(Limelight::Builtin::Players::Image, @prop)
  end

  it "should have a ImagePanel" do
    image_panel = @prop.panel.children[0]
    image_panel.class.should == Limelight::UI::Model::ImagePanel
    @prop.image_panel.should be(image_panel)
  end

  it "should have an image" do
    @prop.image = "some/path_to/image.png"
    @prop.panel.children[0].imageFile.should == "some/path_to/image.png";
    @prop.image.should == "some/path_to/image.png";
  end

  it "should have rotation" do
    @prop.rotation = 180
    @prop.rotation.should == 180
    @prop.image_panel.rotation.should == 180
  end

  it "should have scaled" do
    @prop.scaled?.should == true
    @prop.image_panel.scaled.should == true

    @prop.scaled = false;

    @prop.scaled?.should == false
    @prop.image_panel.scaled.should == false
  end

end