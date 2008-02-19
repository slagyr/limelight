module StartButton
  
  def self.extended(block)
    puts "START_BUTTON EXTENDED"
  end
  
  def button_pressed(e)
    puts "starting"
    
    plane = page.find("plane")
    plane.run
  end
  
end