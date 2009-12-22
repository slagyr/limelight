#- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Builtin
    module Players

      # A Builtin Player that adds the look and behavior of a native button.  It may be applied in the PropBuilder DSL
      # like so:
      #
      #   my_button :players => "button"
      #
      # Props including this Player should implement the button_pressed hook.
      #
      #   def button_pressed(e)
      #     # do something
      #   end
      #
      # Props including this Player may implement any of the key and focus event hooks:
      #
      #   key_pressed, key_typed, key_released, focus_gained, focus_lost
      #
      module Button2
        class << self

          def extended(prop) #:nodoc:
            button = Limelight::UI::Model::Inputs::Button2Panel.new
            prop.panel.add(button)
          end
        end

      end

    end
  end
end