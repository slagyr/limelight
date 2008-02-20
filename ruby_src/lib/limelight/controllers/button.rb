module Limelight
  module Controllers
    
    module Button
      class << self
        def extended(block)  
          block.panel.painters.clear
          painter = Java.limelight.ui.painting.ButtonPainter.new(block.panel)
          block.button = painter.button
          block.panel.painters << painter
          block.panel.clear_event_listeners
          set_default_styles(block)
        end
        
        def set_default_styles(block)
          block.style.width = "100"
          block.style.height = "25"
        end
      end
  
      attr_accessor :button
      
    end
  
  end
end