#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Builtin
    module Players

      # A Builtin Player that adds the look and behavior of a native text box.  It may be applied in the PropBuilder DSL
      # like so:
      #
      #   my_text_box :players => "text_box"
      #
      # Props including this Player may implement any of the key and focus event hooks:
      #
      #   key_pressed, key_typed, key_released, focus_gained, focus_lost
      #
      module TextBox
        class << self

          def extended(prop) #:nodoc:
            text_box = Limelight::UI::Model::Inputs::TextBoxPanel.new
            prop.panel.add(text_box)
            prop.text_box = text_box.text_box
          end

        end

        attr_accessor :text_box #:nodoc:

      end

    end
  end
end