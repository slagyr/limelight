#- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

on_mouse_entered do
  start_fading(100, -1)
end

on_mouse_exited do
  start_fading(0, 1)
end

def start_fading(start, direction)
  return if !@animation.nil? && @animation.running?
  @transparency = start
  @animation = animate(:updates_per_second => 50) do
    style.transparency = @transparency.to_s
    @transparency += direction
    @animation.stop if @transparency > 100 || @transparency < 0
  end
end
