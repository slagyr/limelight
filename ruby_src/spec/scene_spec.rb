require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/scene'

describe Limelight::Scene do

  before(:each) do
    @illuminator = make_mock("illuminator", :fill_cast => nil)
    @scene = Limelight::Scene.new(:illuminator => @illuminator)
  end
  
  it "should have a styles hash" do
    @scene.styles.should_not == nil
    @scene.styles.size.should == 0
  end

  it "should have a button group cache" do
    @scene.button_groups.should_not == nil
    @scene.button_groups.class.should == Java::limelight.ui.ButtonGroupCache
  end
  
  it "should pullout sytles and casting_director from options" do
    scene = Limelight::Scene.new(:styles => "styles", :illuminator => @illuminator)
    
    scene.styles.should == "styles"
    scene.illuminator.should == @illuminator
  end
end
