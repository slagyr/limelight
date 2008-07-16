#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/prop'
require 'limelight/players/check_box'

describe Limelight::Players::CheckBox do

  before(:each) do
    @prop = Limelight::Prop.new
    @prop.add_controller(Limelight::Players::CheckBox)
  end
  
  it "should get rid of the all painters and add a CheckBoxPainter" do
    @prop.panel.painters.size.should == 1
    @prop.panel.painters.last.class.should == Limelight::UI::Model::Painting::CheckBoxPainter
  end
  
  it "should clear event listeners on the panel" do
    @prop.panel.mouse_listeners.length.should == 0
    @prop.panel.key_listeners.length.should == 0
  end
  
  it "should have a TextField" do
    @prop.panel.components[0].class.should == javax.swing.JCheckBox
  end
  
  it "should handled checked state" do
    @prop.checked?.should == false
    @prop.checked.should == false
    
    @prop.checked = true
    
    @prop.checked?.should == true
    @prop.checked.should == true
    @prop.panel.components[0].is_selected.should == true
  end

end