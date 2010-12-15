#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/stage'
require 'limelight/scene'
require 'limelight/theater'

describe Limelight::Stage do

  before(:each) do
    Java::limelight.ui.model.StageFrame.hiddenMode = true
    @peer_production = Java::limelight.model.FakeProduction.new("Fake Production")
    @production = mock("production")
    @theater = Limelight::Theater.new(@production, @peer_production.theater)
    @stage = @theater.add_stage("George")
    Limelight::Context.instance.frameManager = Java::limelight.ui.model.InertFrameManager.new
  end

  it "should have a name" do
    stage = Limelight::Stage.new(@theater, "Jose")
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
    @stage.peer.framed.should == false
    @stage.should_not be_framed

    @stage.framed = true
    @stage.peer.framed.should == true
    @stage.should be_framed
  end

  it "should be always on top" do
    @stage.always_on_top = true
    @stage.should be_always_on_top
    @stage.peer.should be_always_on_top

    @stage.always_on_top = false
    @stage.should_not be_always_on_top
    @stage.peer.should_not be_always_on_top
  end

  it "should set kiosk mode" do
    @stage.kiosk?.should == false
    @stage.peer.should_not be_kiosk

    @stage.kiosk = true
    @stage.kiosk?.should == true
    @stage.peer.should be_kiosk
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
    @stage.peer.isVital.should == false
  end

end
