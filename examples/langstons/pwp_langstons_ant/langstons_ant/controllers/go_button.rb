# Ant is on a black dot, paint white, go right
# Ant is on a white dot, paint black, go left

module GoButton    
  
  def next_id(x, y, direction, color)
    x = x.to_i
    y = y.to_i
    if(direction == :north and color == "black")
      return "#{x + 1}_#{y}", :east
    elsif(direction == :north and color == "white")
      return "#{x - 1}_#{y}", :west
    elsif(direction == :east and color == "black")
      return "#{x}_#{y - 1}", :south
    elsif(direction == :east and color == "white")
      return "#{x}_#{y + 1}", :north
    elsif(direction == :west and color == "black")
      return "#{x}_#{y + 1}", :north
    elsif(direction == :west and color == "white")
      return "#{x}_#{y - 1}", :south
    elsif(direction == :south and color == "black")
      return "#{x - 1}_#{y}", :west
    elsif(direction == :south and color == "white")
      return "#{x + 1}_#{y}", :east
    else
      raise "Combination of direction=#{direction.to_s} and color=#{color} undefined"
    end                            
  end                           
  
  def reverse_color(current_color)
    return "white" if current_color == "black"
    return "black"
  end            
  
  def split_coordinates(element)
    orientations = element.id.split("_")                        
    return orientations[0], orientations[1]
  end
   
  def mouse_clicked(event)      
    number_of_moves = 0 
    puts "Starting mouse clicked"
    
    @direction = :north
    @current_element = page.find("50_50")
    @current_element.style.background_color = "white"

    while true         
      number_of_moves = number_of_moves + 1      
      x, y = split_coordinates(@current_element)
      
      @id, @direction = next_id(x, y, @direction, @current_element.style.background_color)
      
      @current_element = page.find(@id)
      @current_element.style.background_color = reverse_color(@current_element.style.background_color) 
      @current_element.update_now                
      
      current_move = page.find("current_move")
      current_move.text = "Current: #{id.split("_").join(",")}"
      current_move.update_now
      
      move_count = page.find("move_count")
      move_count.text = "Move Count: #{number_of_moves}"
      move_count.update_now
    end
    

  end
  
  
end