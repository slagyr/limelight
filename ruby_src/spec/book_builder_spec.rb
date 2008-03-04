require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/stage_builder'
require 'limelight/stage'

describe Limelight::StageBuilder do

  before(:each) do
    @producer = make_mock("producer")
  end
  
  it "should build a stage" do
    stage = Limelight.build_stage(@producer)
    
    stage.class.should == Limelight::Stage
  end

  it "should allow default scene" do
    stage = Limelight.build_stage(@producer) do
      default_scene :front_scene
    end
    
    stage.default_scene.should == :front_scene
  end
end
