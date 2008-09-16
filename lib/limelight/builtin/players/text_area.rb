#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Builtin
    module Players

      # A Builtin Player that adds the look and behavior of a native text area.  It may be applied in the PropBuilder DSL
      # like so:
      #
      #   my_text_area :players => "text_area"
      #
      # Props including this Player may implement any of the key and focus event hooks:
      #
      #   key_pressed, key_typed, key_released, focus_gained, focus_lost
      #
      module TextArea
        class << self

          def extended(prop) #:nodoc:
            text_area = Limelight::UI::Model::Inputs::TextAreaPanel.new
            prop.panel.add(text_area)
            prop.text_area = text_area.text_area
          end

        end

        attr_accessor :text_area #:nodoc:

      end

    end
  end
end