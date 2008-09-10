#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Builtin
    module Players

      module RadioButton
        class << self
          def extended(prop)
            radio_button = Limelight::UI::Model::Inputs::RadioButtonPanel.new
            prop.panel.add(radio_button)
            prop.radio_button = radio_button.radio_button
          end
        end

        attr_accessor :radio_button
        attr_reader :button_group

        def group=(group_name)
          @button_group = scene.button_groups[group_name]
          @button_group.add(@radio_button)
        end

        def checked=(value)
          @radio_button.selected = value
        end
        alias :selected= :checked=

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