#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/stage'

describe Limelight::Stage do

  before(:each) do
    @theater = make_mock("theater")
    @stage = Limelight::Stage.new(@theater, "George")
  end

  it "should have a name" do
    stage = Limelight::Stage.new(@theater, "Jose")
    stage.theater.should be(@theater)
    stage.name.should == "Jose"
  end

  it "should have a title which default to it's name" do
    @stage.title.should == "George"

    @stage.title = "Once Upon a Test"

    @stage.title.should == "Once Upon a Test"
  end

  it "should have size" do
    @stage.size.should == [800, 800]

    @stage.size = 123, 456

    @stage.size.should == [123, 456]
  end

  it "should have location" do
    @stage.location.should == [200, 25]

    @stage.location = 123, 456

    @stage.location.should == [123, 456]
  end

  it "should not allow name changes" do
    lambda { @stage.name = "new name" }.should raise_error
  end

  it "should set the background color" do
    @stage.background_color = "red"

    @stage.background_color.should == "#FF0000"
  end

  it "should be framed or not" do
    @stage.framed = false
    @stage.frame.should be_undecorated
    @stage.should_not be_framed

    @stage.framed = true
    @stage.frame.should_not be_undecorated
    @stage.should be_framed
  end

  it "should be always on top" do
    @stage.always_on_top = true
    @stage.should be_always_on_top
    @stage.frame.should be_always_on_top

    @stage.always_on_top = false
    @stage.should_not be_always_on_top
    @stage.frame.should_not be_always_on_top
  end

  it "should set kiosk mode" do
    @stage.kiosk?.should == false
    @stage.frame.should_not be_kiosk

    @stage.kiosk = true
    @stage.kiosk?.should == true
    @stage.frame.should be_kiosk
  end

  it "should ask the scene if close is allowed" do
    @stage.should_allow_close.should == true

    scene = mock("scene")
    @stage.stub_current_scene scene
    @stage.should_allow_close.should == true

    scene.stub!(:allow_close?).and_return false
    @stage.should_allow_close.should == false

    scene.stub!(:allow_close?).and_return true
    @stage.should_allow_close.should == true
  end

  describe "when opening a scene" do
    before(:each) do
      @scene = make_mock("scene", :visible= => nil, :illuminate => nil, :stage= => nil, :scene_opened => nil)
      @stage.frame.stub!(:open)
      @stage.stub!(:load_scene)
    end

    it "should call scene.scene_opened at the end of opening a scene" do
      @scene.should_receive(:scene_opened)

      @stage.open(@scene)
    end

    it "should illuminate the scene when opening it" do
      @scene.should_receive(:illuminate)

      @stage.open(@scene)
    end

    it "should set the stage on the scene" do
      @scene.should_receive(:stage=).with(@stage)

      @stage.open(@scene)
    end

    it "should hide and show" do
      @stage.open(@scene)
      @stage.frame.should_receive(:visible=).with(false)
      @stage.hide

      @stage.frame.should_receive(:visible=).with(true)
      @stage.show
    end

  end

end
