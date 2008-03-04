require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/scene'
require 'limelight/prop'
require 'limelight/players/combo_box'

describe Limelight::Players::ComboBox do

  before(:each) do
    @scene = Limelight::Scene.new(:illuminator => make_mock("caster", :fill_cast => nil))
    @prop = Limelight::Prop.new(:scene => @scene)
    @prop.include_player(Limelight::Players::ComboBox)
  end
  
  it "should get rid of the all painters and add a ComboBoxPainter" do
    @prop.panel.painters.size.should == 1
    @prop.panel.painters.last.class.should == Java::limelight.ui.painting.ComboBoxPainter
  end
  
  it "should clear event listeners on the panel" do
    @prop.panel.mouse_listeners.length.should == 0
    @prop.panel.key_listeners.length.should == 0
  end
  
  it "should have a ComboBox" do
    @prop.panel.components[0].class.should == javax.swing.JComboBox
  end
  
  it "should have settable value" do
    @prop.choices = ["1", "2", "3"]
    @prop.value.should == "1"
    
    @prop.value = "3"
    @prop.value.should == "3"
  end

end