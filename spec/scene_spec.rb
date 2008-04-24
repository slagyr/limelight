require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/scene'

describe Limelight::Scene do

  before(:each) do
    @casting_director = make_mock("casting_director", :fill_cast => nil)
    @scene = Limelight::Scene.new(:casting_director => @casting_director)
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
    scene = Limelight::Scene.new(:styles => "styles", :casting_director => @casting_director)
    
    scene.styles.should == "styles"
    scene.casting_director.should == @casting_director
  end
end
