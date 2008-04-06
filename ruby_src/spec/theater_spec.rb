require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/theater'
require 'limelight/stage'

describe Limelight::Theater do

  before(:each) do
    @theater = Limelight::Theater.new
    @producer = make_mock("producer")
    @stage = Limelight::Stage.new(@producer, "default")
  end
  
  it "should allow adding of stages" do
    @theater.add_stage(@stage)
    
    @theater.stages.length.should == 1
    @theater.stages.values[0].should == @stage
  end
  
  it "should not return the actual_list of stages" do
    @theater.stages.should_not be(@theater.stages)
  end
  
  it "should know it's active stage" do
    stage2 = Limelight::Stage.new(@producer, "two")
    
    @theater.add_stage(@stage)
    @theater.add_stage(stage2)
    
    @theater.active_stage.should == nil
    @theater.stage_activated(stage2)
    @theater.active_stage.should == stage2
    @theater.stage_activated(@stage)
    @theater.active_stage.should == @stage
  end
  
  it "should allow recalling stage by name" do
    stage2 = Limelight::Stage.new(@producer, "two")
    stage3 = Limelight::Stage.new(@producer, "three")
    @theater.add_stage(@stage)
    @theater.add_stage(stage2)
    @theater.add_stage(stage3)
  
    @theater["default"].should == @stage
    @theater["two"].should == stage2
    @theater["three"].should == stage3
  
    @theater.stages["default"].should == @stage
    @theater.stages["two"].should == stage2
    @theater.stages["three"].should == stage3
  end
  
  it "should not allow duplicate theater names" do
    stage = Limelight::Stage.new(@producer, "default")
    duplicate = Limelight::Stage.new(@producer, "default")
    
    @theater.add_stage(stage)
    lambda {  @theater.add_stage(stage) }.should raise_error(Limelight::LimelightException, "Duplicate stage name: 'default'")
  end

end
