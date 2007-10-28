require 'limelight_java'

class Block < JBlock
  
  attr_accessor :onclick
  
  def mouseClicked()
    eval(@onclick) if @onclick
  end
  
  def mouseEntered()
  end
  
  def mouseExited()
  end
  
end