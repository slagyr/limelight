#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

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
  
  def value_changed(e)
    print("Value Changed")
  end
  
  def print(value)
    log = scene.find("combo_box_log")
    return if log.nil?
    log.text += value + "\n"
    log.update
    
    results = scene.find("combo_box_results")
    results.text = "Selected value: #{self.value}"
    results.update
  end
  
end