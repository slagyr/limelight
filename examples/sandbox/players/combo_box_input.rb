module ComboBoxInput
    
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
  
  def item_state_changed(e)
    print("Item State Changed")
  end
  
  def print(value)
    log = page.find("combo_box_log")
    return if log.nil?
    log.text += value + "\n"
    log.update
    
    results = page.find("combo_box_results")
    results.text = "Selected value: #{self.value}"
    results.update
  end
  
end