#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight

  # The Pen is acquired from Prop.pen.  It is used to draw directly on the screen withing the bounds of the prop
  # from which the Pen was acquired.
  #
  # All points used by the Pen are relative to the bounds of the Prop.  The top left corner of the Prop is represented
  # by the point (0, 0).  If a Prop has margin, border, or padding, the point (0, 0) may appear to be outside the Prop.  
  #
  class Pen
    
    attr_accessor :context

    # It is constructed with a context which is essentially a java.awt.Graphic2D object.  Defaults are set:
    # * color = "black"
    # * width = 1
    # * smooth = false
    #
    def initialize(context)
      @context = context
      self.color = "black"
      self.width = 1
      self.smooth = false
    end

    # Sets the color of the Pen.  The passed value should be a string that either names a known color or specifies
    # a hex color value.
    #
    def color=(value)
      resolve_color = Util::Colors.resolve(value)
      @context.setColor(resolve_color)
    end

    # Sets the width, in pixels, of the pen.
    #
    def width=(value)
      @context.setStroke(java.awt.BasicStroke.new(value))
    end

    # Specifies whether the pen will use anti-aliasing to draw smooth shapes or not.  Shapes will appear pixilated when
    # smooth is set to false.
    #
    def smooth=(value)
      hint = value ? java.awt.RenderingHints::VALUE_ANTIALIAS_ON : java.awt.RenderingHints::VALUE_ANTIALIAS_OFF
      @context.setRenderingHint(java.awt.RenderingHints::KEY_ANTIALIASING, hint)
    end

    # Draws a line from the point (x1, y1) to the point (x2, y2)
    #
    def draw_line(x1, y1, x2, y2)
      @context.drawLine(x1, y1, x2, y2)
    end

    # Draws a rectangle with the top left corner at (x, y).
    #
    def draw_rectangle(x, y, width, height)
      @context.drawRect(x, y, width, height)
    end

    # Fills a rectangle with the current color of the Pen.  The top left corner of the rectangle is (x, y).
    #
    def fill_rectangle(x, y, width, height)
      @context.fillRect(x, y, width, height)
    end

    # Draws the largest oval that will fit in the specified rectangle.  The top left corner of the bounding
    # rectangle is at (x, y).  The center of the oval will be at (x + width/2, y + height/2).
    #
    def draw_oval(x, y, width, height)
      @context.drawOval(x, y, width, height)
    end

    # Fills an oval specified by the bounding rectangle.  See draw_oval.  
    #
    def fill_oval(x, y, width, height)
      @context.fillOval(x, y, width, height)
    end
    
  end
  
end