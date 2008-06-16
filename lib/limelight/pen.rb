#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  
  class Pen
    
    attr_accessor :context
    
    def initialize(context)
      @context = context
      self.color = "black"
      self.width = 1
      self.smooth = false
    end
    
    def color=(value)
      resolve_color = Util::Colors.resolve(value)
      @context.setColor(resolve_color)
    end
    
    def width=(value)
      @context.setStroke(java.awt.BasicStroke.new(value))
    end
    
    def smooth=(value)
      hint = value ? java.awt.RenderingHints::VALUE_ANTIALIAS_ON : java.awt.RenderingHints::VALUE_ANTIALIAS_OFF
      @context.setRenderingHint(java.awt.RenderingHints::KEY_ANTIALIASING, hint)
    end
    
    def draw_line(x1, y1, x2, y2)
      @context.drawLine(x1, y1, x2, y2)
    end
    
    def draw_rectangle(x, y, width, height)
      @context.drawRect(x, y, width, height)
    end
    
    def fill_rectangle(x, y, width, height)
      @context.fillRect(x, y, width, height)
    end
    
    def draw_oval(x, y, width, height)
      @context.drawOval(x, y, width, height)
    end

    def fill_oval(x, y, width, height)
      @context.fillOval(x, y, width, height)
    end
    
  end
  
end