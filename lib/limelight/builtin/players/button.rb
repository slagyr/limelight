#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Builtin
    module Players

      module Button
        class << self

          def extended(prop)
            button = Limelight::UI::Model::Inputs::ButtonPanel.new
            prop.panel.add(button)
            prop.button = button.button
          end
        end

        attr_accessor :button

      end

    end
  end
end