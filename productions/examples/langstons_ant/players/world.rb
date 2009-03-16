#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module World

  def self.extended(prop)
    prop.cells = {}
  end
  
  attr_accessor :cells

  def draw_grid()
    area = bordered_area
    pen = self.pen
    pen.color = '#bbb'
    x = 6
    99.times do |i|
      pen.draw_line(x, area.top, x, area.bottom)
      pen.draw_line(area.left, x, area.right, x)
      x += 5
    end
  end
  
  def paint_cell(x, y, color)
    area = bordered_area
    pen = self.pen
    x2 = x * 5 + 2  
    y2 = y * 5 + 2
    pen.color = color
    pen.fill_rectangle(x2, y2, 4, 4)
  end
  
  attr_accessor :cell_index
  
end
