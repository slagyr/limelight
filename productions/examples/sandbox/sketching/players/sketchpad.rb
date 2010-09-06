#- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

def activate(mode)
  @mode = mode
end

def clear
  puts "Clearing"
  update
end

on_mouse_pressed do |e|
  self.send(@mode, e.x, e.y) unless @mode.nil?
end

on_mouse_dragged do |e|
  if (@mode == :line && !@line_start.nil?)
    pen.draw_line(@line_start[0], @line_start[1], e.x, e.y)
    @line_start = [e.x, e.y]
  end
end

def line(x, y)
  @line_start = [x, y]
end

def square(x, y)
  pen = self.pen
  pen.width = 2
  pen.color = "blue"
  pen.draw_rectangle(x, y, 25, 25)
end

def circle(x, y)
  pen = self.pen
  pen.smooth = true
  pen.width = 5
  pen.color = "red"
  pen.draw_oval(x, y, 25, 25)
end