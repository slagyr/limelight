module Limelight
  module Controllers
    
    module ComboBox
      class << self
        def extended(block)  
          block.panel.painters.clear
          painter = Java.limelight.ComboBoxPainter.new(block.panel)
          block.combo_box = painter.combo_box
          block.panel.painters << painter
          block.panel.clear_event_listeners
          set_default_styles(block)
        end
        
        def set_default_styles(block)
          block.style.width = "120"
          block.style.height = "22"
        end
      end
      
      attr_accessor :combo_box
      
      def choices=(value)
        value = eval(value) if value.is_a? String
        raise "Invalid choices type: #{value.class}.  Choices have to be an array." if !value.is_a?(Array)
        combo_box.remove_all_items
        value.each { |choice| combo_box.add_item(choice) }
      end
      
      def value=(text)
        self.text = text
      end
      
      def value
        return self.text
      end
      
    end
  
  end
end