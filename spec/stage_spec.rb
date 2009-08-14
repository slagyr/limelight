#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/stage'
require 'limelight/scene'
require 'limelight/theater'

describe Limelight::Stage do

  before(:each) do
    @theater = Limelight::Theater.new
    @stage = @theater.add_stage("George")
    @stage.should_remain_hidden = true
    Limelight::Context.instance.frameManager = Java::limelight.ui.model.InertFrameManager.new
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
    @stage.size.should == ["500", "500"]

    @stage.size = 123, 456
    @stage.size.should == ["123", "456"]

    @stage.size = [123, 456]
    @stage.size.should == ["123", "456"]

    @stage.size = "50%", "100%"
    @stage.size.should == ["50%", "100%"]
  end

  it "should have location" do
    @stage.location.should == ["center", "center"]

    @stage.location = 123, 456
    @stage.location.should == ["123", "456"]

    @stage.location = [123, 456]
    @stage.location.should == ["123", "456"]

    @stage.location = "left", "top"
    @stage.location.should == ["left", "top"]

    @stage.location = "20%", "40%"
    @stage.location.should == ["20%", "40%"]
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

  it "should allow contructor options" do
    stage = Limelight::Stage.new(@theater, "Ringo", :framed => false, :background_color => "blue")
    stage.framed?.should == false
    stage.background_color.should == "#0000FF"
  end

  it "should handle vitality" do
    @stage.vital?.should == true
    @stage.vital = false
    @stage.vital?.should == false
    @stage.frame.isVital.should == false
  end

  describe "when opening a scene" do
    before(:each) do
      @scene = Limelight::Scene.new
      @scene.stub!(:illuminate)
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
      @stage.should_remain_hidden = false
      @stage.frame.should_receive(:visible=).with(false)
      @stage.hide

      @stage.frame.should_receive(:visible=).with(true)
      @stage.show
    end

    it "should open the frame when opening" do
      @stage.should_remain_hidden = false
      @stage.frame.should_receive(:open)

      @stage.open(@scene)
    end

    it "should not open the frame when opening and in hidden mode" do
      @stage.should_remain_hidden = true
      @stage.frame.should_not_receive(:open)

      @stage.open(@scene)
    end

    it "should clean up on close" do
      @stage.open(@scene)

      @stage.close

      @scene.visible?.should == false
      @stage.current_scene.should == nil
    end

    it "should be removed from the theater when closed" do
      @stage.open(@scene)
      @theater["George"].should be(@stage)

      @stage.close      
      @theater["George"].should == nil
    end

  end

end
