#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/stage'
require 'limelight/scene'
require 'limelight/theater'

describe Limelight::Stage do

  before(:each) do
    @production = mock("production")
    @theater = Limelight::Theater.new(@production)
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

    @stage.background_color.should == "#ff0000ff"
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
    stage.background_color.should == "#0000ffff"
  end

  it "should handle vitality" do
    @stage.vital?.should == true
    @stage.vital = false
    @stage.vital?.should == false
    @stage.frame.isVital.should == false
  end

  it "should notify theater when activated" do
    @theater.should_receive(:stage_activated).with(@stage)
    @stage.activated(nil)
  end

  it "should respond to stage events" do
    e = mock("window event")
    @theater.stub!(:stage_deactivated)
    @theater.stub!(:stage_closed)
    
    @stage.activated(e)
    @stage.deactivated(e)
    @stage.iconified(e)
    @stage.deiconified(e)
    @stage.closing(e)
    @stage.closed(e)
  end

  it "should notify theater upon deactivation" do
    @theater.should_receive(:stage_deactivated).with(@stage)
    @stage.deactivated(nil)
  end

  describe "when opening a scene" do

    before(:each) do
      @production.stub!(:theater_empty!)
      Limelight::Studio.install
      @scene = Limelight::Scene.new
      @scene.stub!(:illuminate)
    end

    it "should call scene.scene_opened at the end of opening a scene" do
      event = mock("scene_opened event") 
      Limelight::UI::Events::SceneOpenedEvent.should_receive(:new).with(@scene.panel).and_return(event)
      @scene.panel.event_handler.should_receive(:dispatch).with(event)

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

    it "should close by closing the frame" do
      @stage.frame.should_receive(:close)

      @stage.close
    end

    it "should clean when closed" do
      @stage.open(@scene)

      @stage.closed(nil)

      @scene.visible?.should == false
      @stage.current_scene.should == nil
    end

    it "should clean up when replacing scene" do
      @stage.open(@scene)
      new_scene = Limelight::Scene.new(:name => "new scene")
      new_scene.stub!(:illuminate)

      @stage.open(new_scene)

      @scene.visible?.should == false
      @stage.current_scene.should == new_scene
    end

    it "should be removed from the theater when closed" do
      @stage.open(@scene)
      @theater["George"].should be(@stage)

      @stage.closed(nil)
      @theater["George"].should == nil
    end

    it "should open an alert" do
      utilities_production = mock("utilities_production")
      Limelight::Context.instance.studio.should_receive(:utilities_production).and_return(utilities_production)              
      utilities_production.should_receive(:alert).with("Some Message")

      @stage.alert("Some Message")
      sleep(0.01)
    end

  end

end
