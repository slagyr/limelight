#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Players
    
    module TextBox
      class << self
        def extended(prop)
          prop.panel.painters.clear
          prop.panel.painters << Java.limelight.ui.painting.TextBoxPainter.new(prop.panel)
          prop.panel.clear_event_listeners
          set_default_styles(prop)
        end
        
        def set_default_styles(prop)
          prop.style.width = "120"
          prop.style.height = "22"
        end
      end
  
    end
  
  end
end