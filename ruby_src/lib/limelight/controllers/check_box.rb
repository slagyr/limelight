module Limelight
  module Controllers
    
    module CheckBox
      class << self
        def extended(block)  
          block.panel.painters.clear
          painter = Java.limelight.CheckBoxPainter.new(block.panel)
          block.check_box = painter.check_box
          block.panel.painters << painter
          block.panel.clear_event_listeners
          set_default_styles(block)
        end
        
        def set_default_styles(block)
          block.style.width = "22"
          block.style.height = "22"
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