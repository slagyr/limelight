#- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

on_mouse_entered do
  @step = -1
  @animation = animate(:updates_per_second => 30) do
    @step = -1 if style.gradient_penetration == "100%"
    @step = 1 if style.gradient_penetration == "1%"
    style.gradient_penetration = (style.gradient_penetration.to_i + @step).to_s
  end
end

on_mouse_exited do
  @animation.stop
end
  