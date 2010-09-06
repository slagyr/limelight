#- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

on_cast do
  @animation = animate(:updates_per_second => 1) do
    refloat
  end
end

def go(x, y)
  @float_x = x
  @float_y = y
  refloat
end

private

def refloat
  return unless @float_x
  style.x = float_x
  style.y = float_y
end

def float_x
  return 0 if @float_x == :left
  return (parent.child_area.width - area.width) / 2 if @float_x == :center
  return parent.child_area.width - area.width
end

def float_y
  return 0 if @float_y == :top
  return (parent.child_area.height - area.height) / 2 if @float_y == :center
  return parent.child_area.height - area.height
end
