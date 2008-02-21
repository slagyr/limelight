module Cell2
  
  def self.extended(block)
  end
  
  def mouse_clicked(e)
    puts "mouse clicked"
    p = parent
    parent.remove(self)
    p.update
  end
  
end