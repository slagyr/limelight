#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Players
    
    module TextBox
      class << self

        def extended(prop)
          text_box = Limelight::UI::Model::Inputs::TextBoxPanel.new
          prop.panel.add(text_box)
          prop.text_box = text_box.text_box
          set_default_styles(prop)
        end
        
        def set_default_styles(prop)
          prop.style.width = "120"
          prop.style.height = "28"
        end

      end

      attr_accessor :text_box
  
    end
  
  end
end