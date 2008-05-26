#- Copyright 2008 8th Light, Inc.
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
  
  it "should have size" do
    @stage.location.should == [200, 25]
    
    @stage.location = 123, 456
    
    @stage.location.should == [123, 456]
  end
  
  it "should not allow name changes" do
    lambda { @stage.name = "new name" }.should raise_error
  end

  it "should call scene.scene_opened at the end of opening a scene" do
    scene = make_mock("scene", :visible= => nil)
    scene.should_receive(:scene_opened)

    @stage.frame.stub!(:open)
    @stage.stub!(:load_scene)
    @stage.open(scene)
  end

end
