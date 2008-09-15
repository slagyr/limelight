#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/theater'
require 'limelight/stage'

describe Limelight::Theater do

  before(:each) do
    @theater = Limelight::Theater.new
    @__stage__ = Limelight::Stage.new(@theater, "default")
  end
  
  it "should allow adding of stages" do
    @theater.add_stage(@__stage__)
    
    @theater.stages.length.should == 1
    @theater.stages[0].should == @__stage__
  end
  
  it "should not return the actual_list of stages" do
    @theater.stages.should_not be(@theater.stages)
  end
  
  it "should know it's active stage" do
    stage2 = Limelight::Stage.new(@theater, "two")
    
    @theater.add_stage(@__stage__)
    @theater.add_stage(stage2)
    
    @theater.active_stage.should == nil
    @theater.stage_activated(stage2)
    @theater.active_stage.should == stage2
    @theater.stage_activated(@__stage__)
    @theater.active_stage.should == @__stage__
  end
  
  it "should allow recalling stage by name" do
    stage2 = Limelight::Stage.new(@theater, "two")
    stage3 = Limelight::Stage.new(@theater, "three")
    @theater.add_stage(@__stage__)
    @theater.add_stage(stage2)
    @theater.add_stage(stage3)
  
    @theater["default"].should == @__stage__
    @theater["two"].should == stage2
    @theater["three"].should == stage3
  end
  
  it "should not allow duplicate theater names" do
    stage = Limelight::Stage.new(@theater, "default")
    duplicate = Limelight::Stage.new(@theater, "default")
    
    @theater.add_stage(stage)
    lambda {  @theater.add_stage(stage) }.should raise_error(Limelight::LimelightException, "Duplicate stage name: 'default'")
  end
  
  it "should have a default stage" do
    @theater.default_stage.name.should == "Limelight"
    @theater.default_stage.theater.should == @theater
    
    @theater.default_stage.should be(@theater.default_stage)
  end

end
