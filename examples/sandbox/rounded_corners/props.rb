__ :class_name => "sandbox"
__install "header.rb"
arena do
  row do
    5.times do |i|
      box :rounded_corner_radius => (i * 10).to_s
    end
  end
  row do
    5.times do |i|
      box :rounded_corner_radius => (i * 10).to_s, :background_color => "red", :border_color => "#00fa"
    end
  end
  row do
    5.times do |i|
      box :rounded_corner_radius => (i * 10).to_s, :border_width => "0", :background_color => "red", :secondary_background_color => "green", :gradient_angle => "0", :gradient_penetration => "100"
    end
  end
end