#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/pen'

describe Limelight::Pen do

  before(:each) do
    @context = mock("context", :setColor => nil, :setStroke => nil, :setRenderingHint => nil)
    @pen = Limelight::Pen.new(@context)
  end
  
  it "should default to a think black stroke" do
    @context.should_receive(:setColor).with(java.awt.Color::black)
    @context.should_receive(:setStroke).with(java.awt.BasicStroke.new(1))
    @context.should_receive(:setRenderingHint).with(java.awt.RenderingHints::KEY_ANTIALIASING, java.awt.RenderingHints::VALUE_ANTIALIAS_OFF)
    
    @pen = Limelight::Pen.new(@context)
    
    @pen.context.should be(@context)
  end
  
  it "should allow setting the color" do
    @context.should_receive(:setColor).with(java.awt.Color::blue)
  
    @pen.color = "blue"
  end
  
  it "should allow setting the width" do
    @context.should_receive(:setStroke).with(java.awt.BasicStroke.new(5))
    
    @pen.width = 5
  end
  
  it "should allow setting smooth on" do
    @context.should_receive(:setRenderingHint).with(java.awt.RenderingHints::KEY_ANTIALIASING, java.awt.RenderingHints::VALUE_ANTIALIAS_ON)
    
    @pen.smooth = true
  end
  
  it "should draw a line" do
    @context.should_receive(:drawLine).with(1, 2, 3, 4)
    
    @pen.draw_line(1, 2, 3, 4)
  end
  
  it "should draw a rectangle" do
    @context.should_receive(:drawRect).with(1, 2, 3, 4)
    
    @pen.draw_rectangle(1, 2, 3, 4)
  end
  
  it "should fill a rectangle" do
    @pen.color = "blue"
    
    @context.should_receive(:fillRect).with(1, 2, 3, 4)
    
    @pen.fill_rectangle(1, 2, 3, 4)
  end

end
