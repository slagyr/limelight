#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

__ :name => "sandbox"
__install "header.rb"
arena do
  table :scroll_bars => "on" do
    10.times do |y|
      row :id => y.to_s do
        10.times do |x|
          id = "#{x},#{y}"
          bg_color = ( (x + y) % 2 == 0 ) ? "blue" : "#DDDDDD"
          cell :id => id, :text => id, :background_color => bg_color
        end
      end
    end
  end
  table :vertical_scroll_bar => "off", :horizontal_scroll_bar => "on" do
    2.times do |y|
      row :id => y.to_s do
        10.times do |x|
          id = "#{x},#{y}"
          bg_color = ( (x + y) % 2 == 0 ) ? "blue" : "#DDDDDD"
          cell :id => id, :text => id, :background_color => bg_color
        end
      end
    end
  end
  table :vertical_scroll_bar => "on", :horizontal_scroll_bar => "off" do
    10.times do |y|
      row :id => y.to_s do
        2.times do |x|
          id = "#{x},#{y}"
          bg_color = ( (x + y) % 2 == 0 ) ? "blue" : "#DDDDDD"
          cell :id => id, :text => id, :background_color => bg_color
        end
      end
    end
  end                                       
end