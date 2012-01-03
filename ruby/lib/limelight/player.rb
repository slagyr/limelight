#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

module Limelight
  class Player < Module

    include Java::limelight.model.api.Player

    def initialize(player_name, player_path)
      @name = player_name
      @path = player_path
      @__event_cache = {}
    end

    def cast(prop)
      prop = prop.is_a?(Limelight::Prop) ? prop : prop.proxy
      unless prop.is_a?(self)
        prop.extend(self)
      end
    end

    def path
      @path
    end
    alias getPath path

    def name
      @name
    end
    alias getName name

    def applyOptions(prop, options)
      # nothing to do here because options are already applied to mixed in methods
      options
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
      __add_action(Java::limelight.ui.events.panel.MousePressedEvent, action)
    end

    def on_mouse_released(& action)
      __add_action(Java::limelight.ui.events.panel.MouseReleasedEvent, action)
    end

    def on_mouse_clicked(& action)
      __add_action(Java::limelight.ui.events.panel.MouseClickedEvent, action)
    end

    def on_mouse_entered(& action)
      __add_action(Java::limelight.ui.events.panel.MouseEnteredEvent, action)
    end

    def on_mouse_exited(& action)
      __add_action(Java::limelight.ui.events.panel.MouseExitedEvent, action)
    end

    def on_mouse_moved(& action)
      __add_action(Java::limelight.ui.events.panel.MouseMovedEvent, action)
    end

    def on_mouse_wheel(& action)
      __add_action(Java::limelight.ui.events.panel.MouseWheelEvent, action)
    end

    def on_mouse_dragged(& action)
      __add_action(Java::limelight.ui.events.panel.MouseDraggedEvent, action)
    end

    def on_key_pressed(& action)
      __add_action(Java::limelight.ui.events.panel.KeyPressedEvent, action)
    end

    def on_key_released(& action)
      __add_action(Java::limelight.ui.events.panel.KeyReleasedEvent, action)
    end

    def on_char_typed(& action)
      __add_action(Java::limelight.ui.events.panel.CharTypedEvent, action)
    end

    def on_focus_gained(& action)
      __add_action(Java::limelight.ui.events.panel.FocusGainedEvent, action)
    end

    def on_focus_lost(& action)
      __add_action(Java::limelight.ui.events.panel.FocusLostEvent, action)
    end

    def on_button_pushed(& action)
      __add_action(Java::limelight.ui.events.panel.ButtonPushedEvent, action)
    end

    def on_value_changed(& action)
      __add_action(Java::limelight.ui.events.panel.ValueChangedEvent, action)
    end

    def on_scene_opened(& action)
      __add_action(Java::limelight.ui.events.panel.SceneOpenedEvent, action)
    end

    def on_illuminated(& action)
      __add_action(Java::limelight.ui.events.panel.IlluminatedEvent, action)
    end

    def prop_reader(*symbols)
      symbols.each do |sym|
        module_eval("def #{sym}; return scene.find('#{sym}'); end")
      end
    end

    def backstage_reader(*symbols)
      symbols.each do |sym|
        name = Java::limelight.util.StringUtil.spearCase(sym.to_s)
        module_eval("def #{sym}; return backstage['#{name}']; end")
      end
    end

  end
end