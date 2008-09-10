#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Builtin
    module Players

      module ComboBox
        class << self
          def extended(prop)
            combo_box = Limelight::UI::Model::Inputs::ComboBoxPanel.new
            prop.panel.add(combo_box)
            prop.combo_box = combo_box.combo_box
          end
        end

        attr_accessor :combo_box

        def choices=(value)
          value = eval(value) if value.is_a? String
          raise "Invalid choices type: #{value.class}.  Choices have to be an array." if !value.is_a?(Array)
          @values = value
          combo_box.remove_all_items
          value.each { |choice| combo_box.add_item(choice) }
        end

        def value=(text)
          self.text = text
        end

        def value
          return self.text
        end

        def mouse_pressed(e)
          curtains = Limelight::Prop.new(:name => "limelight_builtin_players_curtains", :players => "curtains")

          popup_list = Limelight::Prop.new(:name => "limelight_builtin_players_combo_box_popup_list", :players => "combo_box_popup_list", :curtains => curtains)
          popup_style = popup_list.style
          popup_style.x = panel.absolute_location.x.to_s
          popup_style.y = panel.absolute_location.y.to_s
          popup_style.width = panel.width.to_s

          @values.each do |value|
            popup_list.add(Limelight::Prop.new(:name => "limelight_builtin_players_combo_box_popup_list_item", :text => value, :players => "combo_box_popup_list_item", :combo_box => self))
          end

          curtains.add(popup_list)
          scene.add(curtains)
        end

      end
    end
  end
end