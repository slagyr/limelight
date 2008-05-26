#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Spinner
  
  def mouse_entered(e)
    @active = true
    @thread = Thread.new { spin }
  end
  
  def mouse_exited(e)
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