#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Builtin
    module Players

      # A Builtin Player that adds the look and behavior of a native password box.  It may be applied in the PropBuilder DSL
      # like so:
      #
      #   my_password_box :players => "password_box"
      #
      # Props including this Player may implement any of the key and focus event hooks:
      #
      #   key_pressed, key_typed, key_released, focus_gained, focus_lost
      #
      module PasswordBox
        class << self

          def extended(prop) #:nodoc:
            password_box = Limelight::UI::Model::Inputs::PasswordBoxPanel.new
            prop.panel.add(password_box)
            prop.password_box = password_box.password_box
          end

        end

        attr_accessor :password_box #:nodoc:

      end

    end
  end
end