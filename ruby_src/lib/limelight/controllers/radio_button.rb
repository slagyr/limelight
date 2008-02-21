module Limelight
  module Controllers
    
    module RadioButton
      class << self
        def extended(block)  
          block.panel.painters.clear
          painter = Java.limelight.ui.painting.RadioButtonPainter.new(block.panel)
          block.radio_button = painter.radio_button
          block.panel.painters << painter
          block.panel.clear_event_listeners
          set_default_styles(block)
        end
        
        def set_default_styles(block)
          block.style.width = "22"
          block.style.height = "22"
        end
      end
  
      attr_accessor :radio_button
      attr_reader :button_group

      def group=(group_name)
        @button_group = page.button_groups[group_name]
        @button_group.add(@radio_button)
      end
      
      def checked=(value)
        @radio_button.selected = value
      end
      alias :selected= :checked=
      
      def checked
        return @radio_button.is_selected
      end
      alias :checked? :checked
      alias :selected? :checked
      alias :selected :checked
      
    end
  
  end
end