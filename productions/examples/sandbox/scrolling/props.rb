#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

__ :name => "sandbox"
__install "header.rb"
arena do
  table :scrollbars => "on", :id => "one_table" do
    10.times do |y|
      row :id => "a_#{y}" do
        10.times do |x|
          id = "a_#{x},#{y}"
          bg_color = ( (x + y) % 2 == 0 ) ? "blue" : "#DDDDDD"
          cell :id => id, :text => "#{x},#{y}", :background_color => bg_color
        end
      end
    end
  end
  table :vertical_scrollbar => "off", :horizontal_scrollbar => "on", :id => "two_table" do
    2.times do |y|
      row :id => "b_#{y}" do
        10.times do |x|
          id = "b_#{x},#{y}"
          bg_color = ( (x + y) % 2 == 0 ) ? "blue" : "#DDDDDD"
          cell :id => id, :text => "#{x},#{y}", :background_color => bg_color
        end
      end
    end
  end
  table :vertical_scrollbar => "on", :horizontal_scrollbar => "off", :id => "three_table" do
    10.times do |y|
      row :id => "c_#{y}" do
        2.times do |x|
          id = "c_#{x},#{y}"
          bg_color = ( (x + y) % 2 == 0 ) ? "blue" : "#DDDDDD"
          cell :id => id, :text => "#{x},#{y}", :background_color => bg_color
        end
      end
    end
  end
  buttons_panel do
    add_button :id => "one_button", :text => "Add Cell", :players => "button"
    add_button :id => "two_button", :text => "Add Cell", :players => "button"
    add_button :id => "three_button", :text => "Add Cell", :players => "button"
  end
end