module CheckBoxInput
    
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
    print("pressed")
  end
  
  def print(value)
    log = page.find("check_box_log")
    log.text += value + "\n"
    log.update
    
    results = page.find("check_box_results")
    results.text = self.checked? ? "checked" : "unchecked"
    results.update
  end
  
end