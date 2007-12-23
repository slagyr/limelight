module Limelight
  module Controllers
    
    module Textbox
      class << self
        def extended(extended_block)     
          text_painter = extended_block.panel.painters.find { |painter| painter.class == Java::limelight.TextPainter }
          extended_block.panel.painters.remove(text_painter) if text_painter
          extended_block.panel.painters << Java.limelight.TextboxPainter.new(extended_block.panel)
        end
      end
  
    end
  
  end
end