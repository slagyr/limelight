module Limelight
  module Players
    
    module RadioButton
      class << self
        def extended(prop)
          prop.panel.painters.clear
          painter = Java.limelight.ui.painting.RadioButtonPainter.new(prop.panel)
          prop.radio_button = painter.radio_button
          prop.panel.painters << painter
          prop.panel.clear_event_listeners
          set_default_styles(prop)
        end
        
        def set_default_styles(prop)
          prop.style.width = "22"
          prop.style.height = "22"
        end
      end
  
      attr_accessor :radio_button
      attr_reader :button_group

      def group=(group_name)
        @button_group = scene.button_groups[group_name]
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