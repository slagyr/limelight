#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Players
    
    module TextBox
      class << self

        def extended(prop)
          prop.panel.painters << Limelight::UI::Model::Painting::TextBoxPainter.new(prop.panel)
          set_default_styles(prop)
        end
        
        def set_default_styles(prop)
          prop.style.width = "120"
          prop.style.height = "28"
        end
      end
  
    end
  
  end
end