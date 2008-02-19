module Antville
  def self.extended(block)
    puts "extending antville"
    @cells = {}
    100.times do |row|
      100.times do |column|
        cell = Limelight::Block.new(:class_name=>"ant_cell", :id=>create_id(row, column))
        block.add_cell(cell)
        block.add(cell)
      end
    end
  end

  def add_cell(cell)
    @cells = {} if not @cells
    @cells[cell.id] = cell
  end
  
  def restart
    @x = 50
    @y = 50
    @velocity = [1,0]
    20000.times { |index|
      # puts index
      move
      }
  end
  
  def find_child(row, col)
    return @cells[Antville.create_id(row, col)]
  end
  
  def self.create_id(row, col)
    "#{row.to_s}|#{col.to_s}"
  end
  
  def wrap(coor)
    if coor == 100
      return 0
    end
    
    if coor == -1
      return 99
    end
    return coor
  end
  
  def move
    @x += @velocity[0]
    @x = wrap(@x)
    @y += @velocity[1]
    @y = wrap(@y)
    
    position = find_child(@x, @y)
    if position.style.background_color == "black"
      position.style.background_color = "white"
      turn_right
    else
      position.style.background_color = "black"
      turn_left
    end
    position.update_now
  end
  
  def turn_left
    turn_vel = [ [1,0], [0,1], [-1,0], [0,-1], [1,0] ]
    cur_index = turn_vel.index(@velocity)
    @velocity = turn_vel[cur_index + 1]
  end
  
  def turn_right
    turn_vel = [ [1,0], [0,-1], [-1,0], [0,1], [1,0] ]
    cur_index = turn_vel.index(@velocity)
    @velocity = turn_vel[cur_index + 1]
  end
  
  
  
  
end
