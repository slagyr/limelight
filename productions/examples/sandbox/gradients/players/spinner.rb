#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Spinner
  
  def mouse_entered(e)
    @animation = animate(:updates_per_second => 30) do
      if style.gradient_angle == "359"
        style.gradient_angle = "0"
      else
        style.gradient_angle = (style.gradient_angle.to_i + 1).to_s
      end
    end
  end
  
  def mouse_exited(e)
    @animation.stop
  end
  
end