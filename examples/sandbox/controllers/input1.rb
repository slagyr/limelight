module Input1
    
  def key_pressed(e)
    print("#{e.key_char} pressed")
  end
  
  def key_typed(e)
    print("#{e.key_char} typed")
  end
  
  def key_released(e)
    print("#{e.key_char} released")
  end
  
  def print(value)
    log = page.find("input1_log")
    log.text += "\n" + value
    log.update
  end
  
end