#- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

FRICTION = 0.1
MAX_DISTANCE = 85

attr_reader :sliding

on_mouse_clicked do
  puts "You caught " + text
end

def width
  @width = style.width.to_i if @width.nil?
  return @width
end

def x
  @x = (style.x.to_i + width/2) if @x.nil?
  return @x
end

def new_x=(value)
  @x = value
  @x = @min_x if @x < @min_x
  @x = @max_x if @x > @max_x
  style.x = (@x.to_i - width/2)
end

def y
  @y = (style.y.to_i + width/2) if @y.nil?
  return @y
end

def new_y=(value)
  @y = value
  @y = @min_y if @y < @min_y
  @y = @max_y if @y > @max_y
  style.y = (@y.to_i - width/2)
end

def center
  return x, y
end

def get_away_from(source_x, source_y)
  return if @sliding
  d = distance(x, y, source_x, source_y)
  return if d > MAX_DISTANCE
  calculate_vector(source_x, source_y)

  @sliding = true
  @animation = animate(:updates_per_second => 30) do
    begin
      slide
    rescue Exception => e
      puts e
      puts e.backtrace
      @animation.stop
    end
  end
end

def calculate_vector(sx, sy)
  dx = (x - sx)
  dy = (y - sy)
  abs_delta = (dx.abs + dy.abs).to_f
  @x_coefficient = dx / abs_delta
  @y_coefficient = dy / abs_delta
  @velocity = 20
  find_bounds if @max_x.nil?
end

def slide
  if @velocity > 1
    @x_coefficient *= -1 if (x <= @min_x || x >= @max_x)
    @y_coefficient *= -1 if (y <= @min_y || y >= @max_y)
    x2 = x + (@x_coefficient * @velocity)
    y2 = y + (@y_coefficient * @velocity)
    self.new_x = x2
    self.new_y = y2
    @velocity -= @velocity * FRICTION
  else
    @sliding = false

    @animation.stop
  end
end

def find_bounds
  @min_x = width/2
  @min_y = width/2
  parent_width = parent.panel.width
  parent_height = parent.panel.height
  @max_x = parent_width - width/2
  @max_y = parent_height - width/2
end

def distance(x1, y1, x2, y2)
  dx = x1 - x2
  dy = y1 - y2
  d = Math.sqrt(dx*dx + dy*dy)
  return d
end
  
