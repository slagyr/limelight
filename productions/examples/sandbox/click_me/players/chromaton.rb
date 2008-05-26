#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

puts "Chromaton required"

module Chromaton
  
  attr_accessor :clicks
  
  def self.extended(extended_block)
    puts "Chromaton extended"
    extended_block.clicks = 0
    extended_block.text = "Click Me!"
  end
  
  def mouse_clicked(e)
    @clicks += 1
    new_random_color
    new_random_size
    update_text
    parent.update
  end
  
  private
  
  def new_random_color
    r = rand(255)
    g = rand(255)
    b = rand(255)
    @new_color = sprintf("#%x%x%x", r, g, b)
    style.background_color = @new_color
  end
  
  def new_random_size
    @size = rand(500) + 50
    style.width = @size.to_s
    style.height = @size.to_s
  end
  
  def update_text
    new_text = "Clicks: #{@clicks}"
    new_text << "\nColor: #{@new_color}"
    new_text << "\nSize: #{@size}"
    self.text = new_text
  end
  
end