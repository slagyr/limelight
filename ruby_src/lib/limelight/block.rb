require 'limelight/limelight_java'

module Limelight
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
end