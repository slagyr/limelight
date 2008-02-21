require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/block'
require 'limelight/players/check_box'

describe Limelight::Players::CheckBox do

  before(:each) do
    @block = Limelight::Block.new
    @block.add_controller(Limelight::Players::CheckBox)
  end
  
  it "should get rid of the all painters and add a CheckBoxPainter" do
    @block.panel.painters.size.should == 1
    @block.panel.painters.last.class.should == Java::limelight.ui.painting.CheckBoxPainter
  end
  
  it "should clear event listeners on the panel" do
    @block.panel.mouse_listeners.length.should == 0
    @block.panel.key_listeners.length.should == 0
  end
  
  it "should have a TextField" do
    @block.panel.components[0].class.should == javax.swing.JCheckBox
  end
  
  it "should handled checked state" do
    @block.checked?.should == false
    @block.checked.should == false
    
    @block.checked = true
    
    @block.checked?.should == true
    @block.checked.should == true
    @block.panel.components[0].is_selected.should == true
  end

end