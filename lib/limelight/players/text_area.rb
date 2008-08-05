#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Players
    
    module TextArea
      class << self
        def extended(prop)
          prop.panel.painters << Limelight::UI::Model::Painting::TextAreaPainter.new(prop.panel)
          set_default_styles(prop)
        end
        
        def set_default_styles(prop)
          prop.style.width ||= "200"
          prop.style.height ||= "88"
        end
      end
  
    end
  
  end
end