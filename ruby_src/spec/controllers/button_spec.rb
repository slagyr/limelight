require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/page'
require 'limelight/block'
require 'limelight/controllers/button'

describe Limelight::Controllers::Button do

  before(:each) do
    @page = Limelight::Page.new
    @block = Limelight::Block.new(:page => @page)
    @block.add_controller(Limelight::Controllers::Button)
  end
  
  it "should get rid of the all painters and add a ButtonPainter" do
    @block.panel.painters.size.should == 1
    @block.panel.painters.last.class.should == Java::limelight.ButtonPainter
  end
  
  it "should clear event listeners on the panel" do
    @block.panel.mouse_listeners.length.should == 0
    @block.panel.key_listeners.length.should == 0
  end
  
  it "should have a Button" do
    @block.panel.components[0].class.should == javax.swing.JButton
  end

end