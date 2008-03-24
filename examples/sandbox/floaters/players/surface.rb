module Surface
  
  def floaters
    @floaters = parent.find_by_class("floater") if @floaters.nil?
    return @floaters
  end
  
  def mouse_moved(e)
    x = e.x
    y = e.y
    floaters.each { |floater| floater.get_away_from(x, y) }
  end
  
end