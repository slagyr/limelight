module Limelight
  module Controllers
    
    module TextBox
      class << self
        def extended(block)  
          # block.panel.painters.clear
          # block.panel.painters << Java.limelight.TextBoxPainter.new(block.panel)
          # block.panel.clear_event_listeners
          block.panel.add(Java.limelight.ui.TextBoxPanel.new())
          set_default_styles(block)
        end
        
        def set_default_styles(block)
          block.style.width = "120"
          block.style.height = "22"
        end
      end
  
    end
  
  end
end