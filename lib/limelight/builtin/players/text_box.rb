#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Builtin
    module Players

      module TextBox
        class << self

          def extended(prop)
            text_box = Limelight::UI::Model::Inputs::TextBoxPanel.new
            prop.panel.add(text_box)
            prop.text_box = text_box.text_box
          end

        end

        attr_accessor :text_box

      end

    end
  end
end