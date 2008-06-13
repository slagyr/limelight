#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/pen'

module Limelight
  
  class PaintAction 
    
    include UI::Painting::PaintAction
    
    attr_reader :block
    
    def initialize(&block)
      @block = block
    end
    
    def invoke(graphics)
      pen = Pen.new(graphics)
      @block.call(pen)
    end
    
  end
  
end