#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Players
    
    module TextArea
      class << self

        def extended(prop)
          text_area = Limelight::UI::Model::Inputs::TextAreaPanel.new
          prop.panel.add(text_area)
          prop.text_area = text_area.text_area
          set_default_styles(prop)
        end
        
        def set_default_styles(prop)
          prop.style.width ||= "200"
          prop.style.height ||= "88"
        end

      end

      attr_accessor :text_area
  
    end
  
  end
end