require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/page'
require 'limelight/block'
require 'limelight/controllers/radio_button'

describe Limelight::Controllers::RadioButton do

  before(:each) do
    @page = Limelight::Page.new
    @block = Limelight::Block.new(:page => @page)
    @block.add_controller(Limelight::Controllers::RadioButton)
  end
  
  it "should get rid of the all painters and add a RadioButtonPainter" do
    @block.panel.painters.size.should == 1
    @block.panel.painters.last.class.should == Java::limelight.ui.painting.RadioButtonPainter
  end
  
  it "should clear event listeners on the panel" do
    @block.panel.mouse_listeners.length.should == 0
    @block.panel.key_listeners.length.should == 0
  end
  
  it "should have a RadioButton" do
    @block.panel.components[0].class.should == javax.swing.JRadioButton
  end
  
  it "should handled checked state" do
    @block.checked?.should == false
    @block.checked.should == false
    @block.selected?.should == false
    @block.selected.should == false
    
    @block.selected = true
    
    @block.checked?.should == true
    @block.checked.should == true
    @block.selected?.should == true
    @block.selected.should == true
    @block.panel.components[0].is_selected.should == true
  end
  
  it "should belong to a button group" do
    @block.group = "group 1"
    
    block2 = Limelight::Block.new(:page => @page)
    block2.add_controller(Limelight::Controllers::RadioButton)
    block2.group = "group 1"
    
    block3 = Limelight::Block.new(:page => @page)
    block3.add_controller(Limelight::Controllers::RadioButton)
    block3.group = "group 2"
    
    group1 = @page.button_groups["group 1"]
    group1.elements.should include(@block.radio_button)
    group1.elements.should include(block2.radio_button)
    group1.elements.should_not include(block3.radio_button)
    
    group2 = @page.button_groups["group 2"]
    group2.elements.should_not include(@block.radio_button)
    group2.elements.should_not include(block2.radio_button)
    group2.elements.should include(block3.radio_button)
    
    @block.button_group.should == group1
    block2.button_group.should == group1
    block3.button_group.should == group2
  end

end