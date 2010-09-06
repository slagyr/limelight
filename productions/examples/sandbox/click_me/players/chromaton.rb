#- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

attr_accessor :clicks

on_cast do
  @clicks = 0
  self.text = "Click Me!"
end

on_mouse_clicked do
  @clicks = 0 if @clicks == nil
  @clicks += 1
  new_random_color
  new_random_size
  update_text
end

private

def new_random_color
  r = rand(255)
  g = rand(255)
  b = rand(255)
  @new_color = "##{as_2_digit_hex(r)}#{as_2_digit_hex(g)}#{as_2_digit_hex(b)}"
  style.background_color = @new_color
end

def as_2_digit_hex(number)
  value = sprintf("%x", number)
  value << "0" if value.length == 1
  return value;
end

def new_random_size
  @size = rand(500) + 50
  style.width = @size
  style.height = @size
end

def update_text
  new_text = "Clicks: #{@clicks}"
  new_text << "\nColor: #{@new_color}"
  new_text << "\nSize: #{@size}"
  self.text = new_text
end
