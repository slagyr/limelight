module Limelight
  module Players
    
    module TextArea
      class << self
        def extended(block)  
          block.panel.painters.clear
          block.panel.painters << Java.limelight.ui.painting.TextAreaPainter.new(block.panel)
          block.panel.clear_event_listeners
          set_default_styles(block)
        end
        
        def set_default_styles(block)
          block.style.width ||= "240"
          block.style.height ||= "88"
        end
      end
  
    end
  
  end
end