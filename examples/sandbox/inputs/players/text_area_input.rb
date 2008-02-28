module TextAreaInput
    
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
  
  def print(value)
    log = page.find("text_area_log")
    log.text += value + "\n"
    log.update
    
    results = page.find("text_area_results")
    results.text = self.text 
    results.update
  end
  
end