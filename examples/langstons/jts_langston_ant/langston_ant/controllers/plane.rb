module Plane

  def self.extended(plane)
    puts "EXTENDING PLANE"
    
    @size = 100
    @@dots = {}
    
    @size.times do |x|
      @size.times do |y|
        dot = Limelight::Block.new(:class_name => "dot", :id => id_for(x, y))
        @@dots[dot.id] = dot
        plane.add(dot)
      end
    end
  end
  
  def run
    @x = @y = 50
    @velocity = [-1,0]
    
    while(true)
      move_ant
      calculate_new_velocity
      color_dot
    end
  end
  
  def move_ant
    @x = normalize(@x + @velocity[0])
    @y = normalize(@y + @velocity[1])
  end

  def calculate_new_velocity
    dot = get_dot(@x, @y)
    
    if dot.style.background_color == "white"
      # go left
      # |x y| *  |  0 +1 |
      #          | -1  0 |
      
      x_vector = -1 * @velocity[1]
      y_vector = @velocity[0]
    else
      # go right
      # |x y| *  |  0 -1 |
      #          | +1  0 |
      
      x_vector = @velocity[1]
      y_vector = -1 * @velocity[0]
    end
    
    @velocity = [x_vector, y_vector]
  end  

  def color_dot
    dot = get_dot(@x, @y)
    if dot.style.background_color == "white"
      dot.style.background_color = "black"
    else
      dot.style.background_color = "white"
    end
    dot.update_now
  end  
    
  def get_dot(x, y)
    return @@dots[Plane::id_for(x, y)]
  end
  
  def normalize(val)
    return 0 if val > 99
    return 99 if val < 0
    return val
  end
  
  def self.id_for(x, y)
    return "dot_#{x}_#{y}"
  end
  
end