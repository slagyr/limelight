#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Builtin
    module Players

      # A Builtin Player that adds the look and behavior of a native check box.  It may be applied in the PropBuilder DSL
      # like so:
      #
      #   my_check_box :players => "check_box"
      #
      # Props including this Player may implement the button_pressed hook.
      #
      #   def button_pressed(e)
      #     # do something
      #   end
      #
      # Props including this Player may implement any of the key and focus event hooks:
      #
      #   key_pressed, key_typed, key_released, focus_gained, focus_lost
      #
      module CheckBox
        class << self
          def extended(prop) #:nodoc:
            check_box = Limelight::UI::Model::Inputs::CheckBoxPanel.new
            prop.panel.add(check_box)
            prop.check_box = check_box.check_box
          end
        end

        attr_accessor :check_box #:nodoc:

        # Will place or remove a check mark in the check box.
        #
        def checked=(value)
          check_box.selected = value
        end

        # Returns true if the check box is checked.
        #
        def checked
          return check_box.selected?
        end
        alias :checked? :checked

      end
    end
  end
end