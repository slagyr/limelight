module RadioButtonInput
    
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
    log = page.find("radio_button_log")
    log.text += "#{id}:#{value}\n"
    log.update
    
    results = page.find("radio_button_results")
    radio1 = page.find("radio_1")
    radio2 = page.find("radio_2")
    radio3 = page.find("radio_3")
    results.text = "One: #{radio1.selected?}\nTwo: #{radio2.selected?}\nThree: #{radio3.selected?}"
    results.update
  end
  
end