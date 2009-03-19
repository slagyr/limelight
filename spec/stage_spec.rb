#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/stage'

describe Limelight::Stage do

  before(:each) do
    @theater = make_mock("theater")
    @__stage__ = Limelight::Stage.new(@theater, "George")
  end
  
  it "should have a name" do
    stage = Limelight::Stage.new(@theater, "Jose")
    stage.theater.should be(@theater)
    stage.name.should == "Jose"
  end
  
  it "should have a title which default to it's name" do
    @__stage__.title.should == "George"
    
    @__stage__.title = "Once Upon a Test"
    
    @__stage__.title.should == "Once Upon a Test"
  end
  
  it "should have size" do
    @__stage__.size.should == [800, 800]
    
    @__stage__.size = 123, 456
    
    @__stage__.size.should == [123, 456]
  end
  
  it "should have location" do
    @__stage__.location.should == [200, 25]
    
    @__stage__.location = 123, 456
    
    @__stage__.location.should == [123, 456]
  end
  
  it "should not allow name changes" do
    lambda { @__stage__.name = "new name" }.should raise_error
  end

  describe "when opening a scene" do
    before(:each) do
      @scene = make_mock("scene", :visible= => nil, :illuminate => nil, :stage= => nil, :scene_opened => nil)
      @__stage__.frame.stub!(:open)
      @__stage__.stub!(:load_scene)
    end

    it "should call scene.scene_opened at the end of opening a scene" do
      @scene.should_receive(:scene_opened)

      @__stage__.open(@scene)
    end

    it "should illuminate the scene when opening it" do
      @scene.should_receive(:illuminate)

      @__stage__.open(@scene)
    end
  
    it "should set the stage on the scene" do
      @scene.should_receive(:stage=).with(@__stage__)

      @__stage__.open(@scene)
    end
  end

end
