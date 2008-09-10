#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

load File.expand_path(File.dirname(__FILE__) + "/ant.rb")
  
module World
  
#   def self.extended(prop)
# create_time = 0
# add_time = 0
#     
#     prop.cell_index = {}
#     50.times do |y|
#       50.times do |x|
#         id = "#{x},#{y}"
# start = Time.now
#         cell = Limelight::Prop.new(:name => "cell", :id => id)
# create_time += Time.now - start
# start = Time.now
#         prop.add(cell)
# add_time += Time.now - start        
#         prop.cell_index[id] = cell
#       end
#     end
# puts "create_time: #{create_time}"    
# puts "add_time: #{add_time}"
# # create_time: 3.2059999999998325
# # add_time: 0.5010000000000002
# 
#   end

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
