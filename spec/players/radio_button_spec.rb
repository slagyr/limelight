#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/scene'
require 'limelight/prop'
require 'limelight/players/radio_button'

describe Limelight::Players::RadioButton do

  before(:each) do
    @scene = Limelight::Scene.new(:casting_director => make_mock("caster", :fill_cast => nil))
    @prop = Limelight::Prop.new
    @scene << @prop
    @prop.include_player(Limelight::Players::RadioButton)
  end
  
  it "should get rid of the all painters and add a RadioButtonPainter" do
#    @prop.panel.painters.size.should == 1
    @prop.panel.painters.last.class.should == Limelight::UI::Model::Painting::RadioButtonPainter
  end
  
#  it "should clear event listeners on the panel" do
#    @prop.panel.mouse_listeners.length.should == 0
#    @prop.panel.key_listeners.length.should == 0
#  end
  
  it "should have a RadioButton" do
    @prop.panel.children[0].class.should == Limelight::UI::Model::Inputs::RadioButtonPanel
  end
  
  it "should handled checked state" do
    @prop.checked?.should == false
    @prop.checked.should == false
    @prop.selected?.should == false
    @prop.selected.should == false
    
    @prop.selected = true
    
    @prop.checked?.should == true
    @prop.checked.should == true
    @prop.selected?.should == true
    @prop.selected.should == true
#    @prop.panel.children[0].is_selected.should == true
  end
  
  it "should belong to a button group" do
    @prop.group = "group 1"
    
    prop2 = Limelight::Prop.new
    @scene << prop2
    prop2.add_controller(Limelight::Players::RadioButton)
    prop2.group = "group 1"
    
    prop3 = Limelight::Prop.new
    @scene << prop3
    prop3.add_controller(Limelight::Players::RadioButton)
    prop3.group = "group 2"
    
    group1 = @scene.button_groups["group 1"]
    group1.elements.should include(@prop.radio_button)
    group1.elements.should include(prop2.radio_button)
    group1.elements.should_not include(prop3.radio_button)
    
    group2 = @scene.button_groups["group 2"]
    group2.elements.should_not include(@prop.radio_button)
    group2.elements.should_not include(prop2.radio_button)
    group2.elements.should include(prop3.radio_button)
    
    @prop.button_group.should == group1
    prop2.button_group.should == group1
    prop3.button_group.should == group2
  end

end