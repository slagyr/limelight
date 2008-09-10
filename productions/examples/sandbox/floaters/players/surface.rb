#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Surface
  
  def floaters
    @floaters = parent.find_by_name("floater") if @floaters.nil?
    return @floaters
  end
  
  def mouse_moved(e)
    x = e.x
    y = e.y
    floaters.each { |floater| floater.get_away_from(x, y) }
  end

  def mouse_clicked(e)
    puts "You missed"
  end
  
end