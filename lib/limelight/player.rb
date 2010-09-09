module Limelight
  class Player < Module

    def self.cast(player, prop)
      unless prop.is_a?(player)
        prop.extend(player)
      end
    end

    def initialize
      @__event_cache = {}
    end

    def __add_action(type, action)
      actions = @__event_cache[type]
      if actions == nil
        actions = @__event_cache[type] = []
      end
      actions << action
    end

    def extended(prop)
      on_cast_actions = @__event_cache[:on_cast]
      if on_cast_actions
        on_cast_actions.each do |action|
          prop.instance_exec(& action) 
        end
      end

      @__event_cache.each do |event, actions|
        if actions && event != :on_cast
          actions.each do |action|
            prop.panel.event_handler.add(event, Proc.new { |e| prop.instance_exec(e, & action) })
          end
        end
      end
    end

    def on_cast(& action)
      __add_action(:on_cast, action)
    end

    def on_mouse_pressed(& action)
      __add_action(Limelight::UI::Events::MousePressedEvent, action)
    end

    def on_mouse_released(& action)
      __add_action(Limelight::UI::Events::MouseReleasedEvent, action)
    end

    def on_mouse_clicked(& action)
      __add_action(Limelight::UI::Events::MouseClickedEvent, action)
    end

    def on_mouse_entered(& action)
      __add_action(Limelight::UI::Events::MouseEnteredEvent, action)
    end

    def on_mouse_exited(& action)
      __add_action(Limelight::UI::Events::MouseExitedEvent, action)
    end

    def on_mouse_moved(& action)
      __add_action(Limelight::UI::Events::MouseMovedEvent, action)
    end

    def on_mouse_wheel(& action)
      __add_action(Limelight::UI::Events::MouseWheelEvent, action)
    end

    def on_mouse_dragged(& action)
      __add_action(Limelight::UI::Events::MouseDraggedEvent, action)
    end

    def on_key_pressed(& action)
      __add_action(Limelight::UI::Events::KeyPressedEvent, action)
    end

    def on_key_released(& action)
      __add_action(Limelight::UI::Events::KeyReleasedEvent, action)
    end

    def on_char_typed(& action)
      __add_action(Limelight::UI::Events::CharTypedEvent, action)
    end

    def on_focus_gained(& action)
      __add_action(Limelight::UI::Events::FocusGainedEvent, action)
    end

    def on_focus_lost(& action)
      __add_action(Limelight::UI::Events::FocusLostEvent, action)
    end

    def on_button_pushed(& action)
      __add_action(Limelight::UI::Events::ButtonPushedEvent, action)
    end

    def on_value_changed(& action)
      __add_action(Limelight::UI::Events::ValueChangedEvent, action)
    end

    def on_scene_opened(& action)
      __add_action(Limelight::UI::Events::SceneOpenedEvent, action)
    end

    def prop_reader(*symbols)
      symbols.each do |sym|
        define_method(sym) do
          return scene.find(sym.to_s)
        end
      end
    end

  end
end