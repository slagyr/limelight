#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/theater'
require 'limelight/stage'

describe Limelight::Theater do

  before(:each) do
    @peer_production = Java::limelight.model.FakeProduction.new("Fake Production")
    @production = mock("production", :theater_empty! => nil)
    @theater = Limelight::Theater.new(@production, @peer_production.theater)
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
  
  it "should allow recalling stage by name" do
    stage2 = @theater.add_stage("two")
    stage3 = @theater.add_stage("three")
  
    @theater["default"].should == @stage
    @theater["two"].should == stage2
    @theater["three"].should == stage3
  end
  
  it "should not allow duplicate theater names" do
    begin
      @theater.add_stage("default")
      false.should == true
    rescue Exception => e
      e.message.include?("Duplicate stage name: 'default'").should == true
    end
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

end
