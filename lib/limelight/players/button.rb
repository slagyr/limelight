#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Players
    
    module Button
      class << self
        def extended(prop)
          painter = Limelight::UI::Model::Painting::ButtonPainter.new(prop.panel)
          prop.button = painter.button
          prop.panel.painters << painter
          set_default_styles(prop)
        end
        
        def set_default_styles(prop)
          prop.style.width = "100"
          prop.style.height = "29"
        end
      end
  
      attr_accessor :button
      
    end
  
  end
end