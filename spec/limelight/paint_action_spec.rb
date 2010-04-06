#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/paint_action'

describe Limelight::PaintAction do
  
  before(:each) do
  end
  
  it "should take a block in constructor" do
    block = Proc.new { |pen| }
    action = Limelight::PaintAction.new(&block)
    
    action.block.should == block
  end
  
  it "should pass pen into the block" do
    local_pen = nil
    action = Limelight::PaintAction.new { |pen| local_pen = pen }
    
    graphics = mock("graphics", :setColor => nil, :setStroke => nil, :setRenderingHint => nil)
    action.invoke(graphics)
    
    local_pen.class.should == Limelight::Pen
    local_pen.context.should == graphics
  end
  
end
