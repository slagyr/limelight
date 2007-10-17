require 'limelight_java'

class Block < JBlock
  
  def initialize(options = {})
    super()
    options.each_pair do |key, value|
      if self.respond_to?((key.to_s + "=").to_s)
        self.send((key.to_s + "=").to_s, value.to_s)
      else
        self.getStyle().send((key.to_s + "=").to_s, value.to_s)
      end
    end
  end

  def <<(value)
    add(value)
    return value
  end
  
  def on_mouse_enter(&block)
    @on_mouse_enter_action = block;
  end
  
  def mouseEntered
    @on_mouse_enter_action.call if @on_mouse_enter_action
  end
  
  def on_mouse_exit(&block)
    @on_mouse_exit_action = block;
  end
  
  def mouseExited
    @on_mouse_exit_action.call if @on_mouse_exit_action
  end
  
  def style(options = {})
    options.each_pair do |key, value|
      self.getStyle().send((key.to_s + "=").to_s, value.to_s)
    end
    panel.repaint
  end
  
end