#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/pen'

module Limelight

  # A PaintAction is created by Prop.after_painting.
  #
  class PaintAction #:nodoc:
    
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