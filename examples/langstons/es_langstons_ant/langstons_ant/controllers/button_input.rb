module ButtonInput
  @@num_moves = 0
  
  def button_pressed(e)
    run_ant
  end
  
  def run_ant
    point = [50, 50]
    velocity = [0, 1]
    
    while true do
      square = page.find("dot_#{point[0]}_#{point[1]}")
      
      if square.style.background_color == "black"
        square.style.background_color = "white"
        velocity = turn_right(velocity)
      else
        square.style.background_color = "black"
        velocity = turn_left(velocity)
      end
      
      square.update_now
      update_num_moves
      update_location(point)
      point = calculate_new_point(point, velocity)
    end
  end
  
  def calculate_new_point(starting_point, velocity)
    return starting_point[0] + velocity[0], starting_point[1] + velocity[1]
  end
  
  def turn_right(velocity)
    case velocity
    when [0, -1] then return [-1, 0]
    when [-1, 0] then return [0, 1]
    when [0, 1] then return [1, 0]
    when [1, 0] then return [0, -1]
    end
  end
  
  def turn_left(velocity)
    case velocity
    when [0, -1] then return [1, 0]
    when [1, 0] then return [0, 1]
    when [0, 1] then return [-1, 0]
    when [-1, 0] then return [0, -1]
    end
  end
  
  def update_num_moves
    @@num_moves += 1
    num_moves = page.find("num_moves_box")
    num_moves.text = "#{@@num_moves}"
    num_moves.update_now
  end
  
  def update_location(point)
    location_box = page.find("location_box")
    location_box.text = "#{point[0]}, #{point[1]}"
    location_box.update_now
  end
end