#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Players
    
    module CheckBox
      class << self
        def extended(prop)
          prop.panel.painters.clear
          painter = Java.limelight.ui.painting.CheckBoxPainter.new(prop.panel)
          prop.check_box = painter.check_box
          prop.panel.painters << painter
          prop.panel.clear_event_listeners
          set_default_styles(prop)
        end
        
        def set_default_styles(prop)
          prop.style.width = "22"
          prop.style.height = "22"
        end
      end
      
      attr_accessor :check_box
      
      def checked=(value)
        check_box.selected = value
      end
      
      def checked
        return check_box.is_selected
      end
      alias :checked? :checked
  
    end
  
  end
end