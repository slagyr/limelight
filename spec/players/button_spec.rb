require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/scene'
require 'limelight/prop'
require 'limelight/players/button'

describe Limelight::Players::Button do

  before(:each) do
    @scene = Limelight::Scene.new(:casting_director => make_mock("caster", :fill_cast => nil))
    @prop = Limelight::Prop.new
    @prop.include_player(Limelight::Players::Button)
  end
  
  it "should get rid of the all painters and add a ButtonPainter" do
    @prop.panel.painters.size.should == 1
    @prop.panel.painters.last.class.should == Java::limelight.ui.painting.ButtonPainter
  end
  
  it "should clear event listeners on the panel" do
    @prop.panel.mouse_listeners.length.should == 0
    @prop.panel.key_listeners.length.should == 0
  end
  
  it "should have a Button" do
    @prop.panel.components[0].class.should == javax.swing.JButton
  end

end