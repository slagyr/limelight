#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/theater'
require 'limelight/stage'

describe Limelight::Theater do

  before(:each) do
    @production = mock("production", :theater_empty! => nil)
    @theater = Limelight::Theater.new(@production)
    @stage = @theater.add_stage("default")
  end
  
  it "should allow adding of stages" do
    @theater.stages.length.should == 1
    @theater.stages[0].should == @stage

    @stage.theater.should == @theater
    @stage.name.should == "default"
  end
  
  it "should not return the actual_list of stages" do
    @theater.stages.should_not be(@theater.stages)
  end
  
  it "should know it's active stage" do
    stage2 = @theater.add_stage("two")
    
    @theater.active_stage.should == nil
    @theater.stage_activated(stage2)
    @theater.active_stage.should == stage2
    @theater.stage_activated(@stage)
    @theater.active_stage.should == @stage
  end
  
  it "should allow recalling stage by name" do
    stage2 = @theater.add_stage("two")
    stage3 = @theater.add_stage("three")
  
    @theater["default"].should == @stage
    @theater["two"].should == stage2
    @theater["three"].should == stage3
  end
  
  it "should not allow duplicate theater names" do
    lambda {  @theater.add_stage("default") }.should raise_error(Limelight::LimelightException, "Duplicate stage name: 'default'")
  end
  
  it "should have a default stage" do
    @theater.default_stage.name.should == "Limelight"
    @theater.default_stage.theater.should == @theater
    
    @theater.default_stage.should be(@theater.default_stage)
  end

  it "should construct stages with options" do
    @theater.add_stage("Joe", :title => "Blowey")

    @theater["Joe"].title.should == "Blowey"
  end

  it "should remove closed stages" do
    @theater.stage_activated(@stage)

    @theater.stage_closed(@stage)

    @theater["default"].should == nil
    @theater.active_stage.should == nil
  end

  it "should notify the production when all the stages are closed" do
    @production.should_receive(:theater_empty!)
    
    @theater.stage_closed(@stage)
  end

  it "should close" do
    stage2 = @theater.add_stage("two")
    stage3 = @theater.add_stage("three")
    @theater.stage_activated(stage3)
    stage2.should_receive(:close)
    stage3.should_receive(:close)

    @theater.close

    @theater.stages.length.should == 0
    @theater.active_stage.should == nil
  end

  it "should deactivate stages" do
    stage2 = @theater.add_stage("two")
    @theater.stage_activated(@stage)
    @theater.stage_deactivated(@stage)
    @theater.active_stage.should == nil

    @theater.stage_activated(stage2)
    @theater.stage_deactivated(@stage) # this is not the active stage
    @theater.active_stage.should == nil  # clear active stage since this implies that stage2 was not really active.   
  end

  it "should notify the production that all the stages are hidden when a stage is closed" do    
    stage2 = @theater.add_stage("two")
    stage2.hide

    @production.should_receive(:theater_empty!)
    @theater.stage_closed(@stage)
  end
  
  it "should notify the production that all the stages are hidden when a stage is deactivated" do
    stage2 = @theater.add_stage("two")
    stage2.hide
    @stage.hide

    @production.should_receive(:theater_empty!)
    @theater.stage_deactivated(@stage)
  end

end
