#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

__ :name => "sandbox"
__install "header.rb"
arena do
  row do
    5.times do |i|
      box :rounded_corner_radius => (i * 10).to_s, :id => "rounded_corner_0_#{i}"
    end
  end
  row do
    5.times do |i|
      box :rounded_corner_radius => (i * 10).to_s, :background_color => "red", :border_color => "#00fa", :id => "rounded_corner_1_#{i}"
    end
  end
  row do
    5.times do |i|
      box :rounded_corner_radius => (i * 10).to_s, :id => "rounded_corner_2_#{i}", :border_width => "0", :background_color => "red", :secondary_background_color => "green", :gradient => "on", :gradient_angle => "0", :gradient_penetration => "100"
    end
  end
end