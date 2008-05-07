__ :name => "sandbox"
__install "header.rb"
arena do
  table do
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
  table do
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
  table do
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