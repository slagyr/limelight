#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Builtin
    module Players

      # A Builtin Player that adds the look and behavior of a native radio button.  It may be applied in the PropBuilder DSL
      # like so:
      #
      #   my_radio_button :players => "radio_button", :id => "radio1", :group => "my_radio_button_group"
      #
      # When including this Player, it's important to specify a group and id.  All radio buttons in the same group will
      # be mutually exclusive.  The value of a radio button group will be the id of the selected radio button.
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
      module RadioButton2
        class << self
          def extended(prop) #:nodoc:
            radio_button = Limelight::UI::Model::Inputs::RadioButton2Panel.new
            prop.panel.add(radio_button)
            prop.radio_button = radio_button
          end
        end

        attr_accessor :radio_button #:nodoc:
        attr_reader :button_group #:nodoc:

        # Sets the radio button group to which this radio button belongs.
        #
        def group=(group_name)
          @button_group = scene.button_groups[group_name]
          @button_group.add(@radio_button)
        end

        # Checks or unchecks this radio button.
        #
        def checked=(value)
          @radio_button.selected = value
        end
        alias :selected= :checked=

        # Returns true is this radio is button is checked.
        #
        def checked
          return @radio_button.is_selected
        end
        alias :checked? :checked
        alias :selected? :checked
        alias :selected :checked

      end

    end
  end
end