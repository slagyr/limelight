require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/block'
require 'limelight/players/text_box'

describe Limelight::Players::TextBox do

  before(:each) do
    @block = Limelight::Block.new
    @block.add_controller(Limelight::Players::TextBox)
  end
  
  it "should get rid of the all painters and add a TextboxPainter" do
    @block.panel.painters.size.should == 1
    @block.panel.painters.last.class.should == Java::limelight.ui.painting.TextBoxPainter
  end
  
  it "should clear event listeners on the panel" do
    @block.panel.mouse_listeners.length.should == 0
    @block.panel.key_listeners.length.should == 0
  end
  
  it "should have a TextField" do
    @block.panel.components[0].class.should == javax.swing.JTextField
  end
  
  it "should use the TextField for the text accessor" do
    @block.text = "blah"
    @block.panel.components[0].text.should == "blah"
    
    @block.panel.components[0].text = "harumph"
    @block.text.should == "harumph"
  end

end