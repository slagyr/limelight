module Spinner
  
  def mouseEntered
    @active = true
    @thread = Thread.new { spin }
  end
  
  def mouseExited 
    @active = false
  end
  
  def spin
    while @active
      style.gradient_angle = (style.gradient_angle.to_i + 1).to_s
      update_now
      sleep(0.02)
    end
  end
  
end