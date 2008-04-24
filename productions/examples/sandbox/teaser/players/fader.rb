module Fader
  
  def self.extended(extended_block)
  end
  
  def mouse_entered(e)
    @color = style.background_color
    @base_alpha = (style.background_color[4..-1] + style.background_color[4..-1]).hex
    @step = (255 - @base_alpha) / 80
    @step = 1 if @step == 0
    @should_loop = true
    @thread = Thread.new { do_shading }
  end
  
  def mouse_exited(e)
    @should_loop = false
    @thread.join
    style.background_color = @color
    update
  end
  
  def do_shading
    while @should_loop
      shade_up 
      shade_down
    end
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
    update_now
  end
  
end