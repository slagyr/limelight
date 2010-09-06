#- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

on_mouse_entered do
  @color = style.background_color
  @base_alpha = @color[7..-1].hex

  @step = (255 - @base_alpha) / 50
  @step = 1 if @step == 0
  @current_alpha = @base_alpha

  @animation = animate(:updates_per_second => 25) do
    @current_alpha += @step
    if @current_alpha < @base_alpha
      @current_alpha = @base_alpha
      @step *= -1
    end
    if @current_alpha > 255
      @current_alpha = 255
      @step *= -1
    end
    update_with_alpha(@current_alpha)
  end
end

on_mouse_exited do
  @animation.stop
  style.background_color = @color
end

def shade_up
  current_alpha = @base_alpha
  while @should_loop and current_alpha < 255
    current_alpha += @step
    update_with_alpha(current_alpha)
  end
end

def shade_down
  current_alpha = 255
  while @should_loop and current_alpha > @base_alpha
    current_alpha -= @step
    update_with_alpha(current_alpha)
  end
end

def update_with_alpha(alpha)
  alpha_string = sprintf("%x", alpha)
  if alpha_string.length == 1
    alpha_string = alpha_string + "0" if alpha > 15
    alpha_string = "0" + alpha_string if alpha < 16
  end
  style.background_color = "#000000#{alpha_string}"
end