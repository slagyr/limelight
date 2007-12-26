require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/block'
require 'limelight/controllers/textbox'

describe Limelight::Controllers::Textbox do

  before(:each) do
    @block = Limelight::Block.new
    @block.add_controller(Limelight::Controllers::Textbox)
  end
  
  it "should get rid of the all painters and add a TextboxPainter" do
    @block.panel.painters.size.should == 1
    @block.panel.painters.last.class.should == Java::limelight.TextboxPainter
  end
  
  it "should clear event listeners on the panel" do
    @block.panel.mouse_listeners.length.should == 0
    @block.panel.key_listeners.length.should == 0
  end

end