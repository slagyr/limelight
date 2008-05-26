#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

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
    log = scene.find("radio_button_log")
    log.text += "#{id}:#{value}\n"
    log.update
    
    results = scene.find("radio_button_results")
    radio1 = scene.find("radio_1")
    radio2 = scene.find("radio_2")
    radio3 = scene.find("radio_3")
    results.text = "One: #{radio1.selected?}\nTwo: #{radio2.selected?}\nThree: #{radio3.selected?}"
    results.update
  end
  
end