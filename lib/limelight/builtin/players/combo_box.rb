#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Builtin
    module Players

      # A Builtin Player that adds the look and behavior of a native combo box.  It may be applied in the PropBuilder DSL
      # like so:
      #
      #   my_button :players => "combo_box"
      #
      # Props including this Player must not override the mouse_pressed event.
      #
      # Props including this Player may implement an additional hook:
      #
      #   def value_changed(e)
      #     # do something
      #   end
      #
      # Props including this Player may implement any of the key and focus event hooks:
      #
      #   key_pressed, key_typed, key_released, focus_gained, focus_lost
      #
      module ComboBox

        class << self
          def extended(prop) #:nodoc:
            combo_box = Limelight::UI::Model::Inputs::ComboBoxPanel.new
            prop.panel.add(combo_box)
            prop.combo_box = combo_box.combo_box
            prop.clear
          end
        end

        attr_accessor :combo_box #:nodoc:
        attr_reader :choices

        def clear #:nodoc:
          @choices = []
        end

        # Sets the choices listed in the combo_box.  The value parameter should an Array or a String that
        # can be evalulated into an Array.  All choices in a combo_box are strings.
        #
        # combo_box.choices = ["one", "two", "three"]
        # combo_box.choices = "['one', 'two', 'three']"
        #
        def choices=(value)
          value = eval(value) if value.is_a? String
          raise "Invalid choices type: #{value.class}.  Choices have to be an array." if !value.is_a?(Array)
          @choices = value
          combo_box.remove_all_items
          value.each { |choice| combo_box.add_item(choice) }
        end

        # Sets the value of the combo box. The value provided should be one choices in the combo box.
        #
        def value=(text)
          self.text = text
        end

        # Returns the value of the currently selected choice.
        #
        def value
          return self.text
        end

        def mouse_pressed(e) #:nodoc:
          curtains = Limelight::Prop.new(:name => "limelight_builtin_players_curtains", :players => "curtains")

          popup_list = Limelight::Prop.new(:name => "limelight_builtin_players_combo_box_popup_list", :players => "combo_box_popup_list", :curtains => curtains)
          popup_style = popup_list.style
          popup_style.x = panel.absolute_location.x.to_s
          popup_style.y = panel.absolute_location.y.to_s
          popup_style.width = panel.width.to_s

          @choices.each do |value|
            popup_list.add(Limelight::Prop.new(:name => "limelight_builtin_players_combo_box_popup_list_item", :text => value, :players => "combo_box_popup_list_item", :combo_box => self))
          end

          curtains.add(popup_list)
          scene.add(curtains)      
        end

      end
    end
  end
end