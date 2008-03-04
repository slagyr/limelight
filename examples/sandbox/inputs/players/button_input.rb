puts "REQUIRED"

module ButtonInput
  
  def self.extended(prop)
    puts "BUTTON INOPUT EXTENDED"
  end
    
  def key_pressed(e)
    print("#{e.key_char} pressed")
  end
  
  def key_typed(e)
    print("#{e.key_char} typed")
  end
  
  def key_released(e)
    print("#{e.key_char} released")
  end
  
  def focus_gained(e)
    print("gained focused")
  end
  
  def focus_lost(e)
    print("lost focus")
  end
  
  def button_pressed(e)
    @presses += 1 if @presses
    @presses = 1 if @presses.nil?
    print("pressed")
  end
  
  def print(value)
    log = page.find("button_log")
    log.text += value + "\n"
    log.update
    
    results = page.find("button_results")
    results.text = "Pressed #{@presses} time(s)."
    results.update
  end
  
end