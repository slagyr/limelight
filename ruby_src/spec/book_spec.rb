require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/stage'
require 'limelight/loaders/file_scene_loader'

describe Limelight::Stage do

  before(:each) do
    @producer = make_mock("producer")
    @stage = Limelight::Stage.new(@producer)
  end
  
  it "should have a producer" do
    @stage.producer.should == @producer
  end
  
  it "should init defaults" do
    @stage.styles.class.should == Hash
    @stage.styles.size.should == 0
  end

end