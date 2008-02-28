module Teaser
  
  def mouse_entered(e)
    @active = true
    @step = -1
    @thread = Thread.new { wave }
  end
  
  def mouse_exited(e)
    @active = false
  end
  
  def wave
    while @active
      @step = -1 if style.gradient_penetration == "100"
      @step = 1 if style.gradient_penetration == "1"
      style.gradient_penetration = (style.gradient_penetration.to_i + @step).to_s
      update_now
      sleep(0.02)
    end
  end
  
end