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
          prop.instance_exec(&action)
        end
      end

      @__event_cache.each do |event, actions|
        if actions && event != :on_cast
          actions.each do |action|
            prop.peer.event_handler.add(event, Proc.new { |e| prop.instance_exec(e, &action) })
          end
        end
      end
    end

    def on_cast(& action)
      __add_action(:on_cast, action)
    end

    def on_mouse_pressed(& action)
      __add_action(Limelight::UI::PanelEvents::MousePressedEvent, action)
    end

    def on_mouse_released(& action)
      __add_action(Limelight::UI::PanelEvents::MouseReleasedEvent, action)
    end

    def on_mouse_clicked(& action)
      __add_action(Limelight::UI::PanelEvents::MouseClickedEvent, action)
    end

    def on_mouse_entered(& action)
      __add_action(Limelight::UI::PanelEvents::MouseEnteredEvent, action)
    end

    def on_mouse_exited(& action)
      __add_action(Limelight::UI::PanelEvents::MouseExitedEvent, action)
    end

    def on_mouse_moved(& action)
      __add_action(Limelight::UI::PanelEvents::MouseMovedEvent, action)
    end

    def on_mouse_wheel(& action)
      __add_action(Limelight::UI::PanelEvents::MouseWheelEvent, action)
    end

    def on_mouse_dragged(& action)
      __add_action(Limelight::UI::PanelEvents::MouseDraggedEvent, action)
    end

    def on_key_pressed(& action)
      __add_action(Limelight::UI::PanelEvents::KeyPressedEvent, action)
    end

    def on_key_released(& action)
      __add_action(Limelight::UI::PanelEvents::KeyReleasedEvent, action)
    end

    def on_char_typed(& action)
      __add_action(Limelight::UI::PanelEvents::CharTypedEvent, action)
    end

    def on_focus_gained(& action)
      __add_action(Limelight::UI::PanelEvents::FocusGainedEvent, action)
    end

    def on_focus_lost(& action)
      __add_action(Limelight::UI::PanelEvents::FocusLostEvent, action)
    end

    def on_button_pushed(& action)
      __add_action(Limelight::UI::PanelEvents::ButtonPushedEvent, action)
    end

    def on_value_changed(& action)
      __add_action(Limelight::UI::PanelEvents::ValueChangedEvent, action)
    end

    def on_scene_opened(& action)
      __add_action(Limelight::UI::PanelEvents::SceneOpenedEvent, action)
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