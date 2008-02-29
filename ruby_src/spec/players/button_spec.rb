require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/page'
require 'limelight/block'
require 'limelight/players/button'

describe Limelight::Players::Button do

  before(:each) do
    @page = Limelight::Page.new(:illuminator => make_mock("caster", :fill_cast => nil))
    @block = Limelight::Block.new
    @block.include_player(Limelight::Players::Button)
  end
  
  it "should get rid of the all painters and add a ButtonPainter" do
    @block.panel.painters.size.should == 1
    @block.panel.painters.last.class.should == Java::limelight.ui.painting.ButtonPainter
  end
  
  it "should clear event listeners on the panel" do
    @block.panel.mouse_listeners.length.should == 0
    @block.panel.key_listeners.length.should == 0
  end
  
  it "should have a Button" do
    @block.panel.components[0].class.should == javax.swing.JButton
  end

end