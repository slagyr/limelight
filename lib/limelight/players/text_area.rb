module Limelight
  module Players
    
    module TextArea
      class << self
        def extended(prop)
          prop.panel.painters.clear
          prop.panel.painters << Java.limelight.ui.painting.TextAreaPainter.new(prop.panel)
          prop.panel.clear_event_listeners
          set_default_styles(prop)
        end
        
        def set_default_styles(prop)
          prop.style.width ||= "240"
          prop.style.height ||= "88"
        end
      end
  
    end
  
  end
end