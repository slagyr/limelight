require 'limelight/pen'

module Limelight
  
  class PaintAction 
    
    include Java::limelight.ui.painting.PaintAction
    
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