module Cell2
  
  def self.extended(prop)
  end
  
  def mouse_clicked(e)
    puts "mouse clicked"
    p = parent
    parent.remove(self)
    p.parent.update # update the whole table incase a row can be removed.
  end
  
end