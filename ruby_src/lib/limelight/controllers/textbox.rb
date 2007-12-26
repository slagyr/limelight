module Limelight
  module Controllers
    
    module Textbox
      class << self
        def extended(block)
          block.panel.painters.clear
          block.panel.painters << Java.limelight.TextboxPainter.new(block.panel)
          block.panel.clear_event_listeners
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