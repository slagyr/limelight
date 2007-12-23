require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/block'
require 'limelight/controllers/textbox'

describe Limelight::Controllers::Textbox do

  before(:each) do
    @block = Limelight::Block.new
    @block.add_controller(Limelight::Controllers::Textbox)
  end
  
  it "should get rid of the TextPainter and add a TextboxPainter" do
    @block.panel.painters.size.should == 3
    @block.panel.painters.find { |painter| painter.class == Java::limelight.TextPainter }.should == nil
    @block.panel.painters.last.class.should == Java::limelight.TextboxPainter
  end

end