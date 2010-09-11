module Limelight

  class Mouse

    def press(prop, x = 0, y = 0, modifiers = 0, click_count = 1)
      location = point_for(prop, x, y)
      owner = owner_of(location, prop)
      Limelight::UI::Events::MousePressedEvent.new(owner, modifiers, location, click_count).dispatch(owner)
    end

    def release(prop, x = 0, y = 0, modifiers = 0, click_count = 1)
      location = point_for(prop, x, y)
      owner = owner_of(location, prop)
      Limelight::UI::Events::MouseReleasedEvent.new(owner, modifiers, location, click_count).dispatch(owner)
    end

    def click(prop, x = 0, y = 0, modifiers = 0, click_count = 1)
      location = point_for(prop, x, y)
      owner = owner_of(location, prop)
      Limelight::UI::Events::MouseClickedEvent.new(owner, modifiers, location, click_count).dispatch(owner)
    end

    def move(prop, x = 0, y = 0, modifiers = 0, click_count = 1)
      location = point_for(prop, x, y)
      owner = owner_of(location, prop)
      Limelight::UI::Events::MouseMovedEvent.new(owner, modifiers, location, click_count).dispatch(owner)
    end

    def drag(prop, x = 0, y = 0, modifiers = 0, click_count = 1)
      location = point_for(prop, x, y)
      owner = owner_of(location, prop)
      Limelight::UI::Events::MouseDraggedEvent.new(owner, modifiers, location, click_count).dispatch(owner)
    end

    def enter(prop, x = 0, y = 0, modifiers = 0, click_count = 1)
      location = point_for(prop, x, y)
      owner = owner_of(location, prop)
      Limelight::UI::Events::MouseEnteredEvent.new(owner, modifiers, location, click_count).dispatch(owner)
    end

    def exit(prop, x = 0, y = 0, modifiers = 0, click_count = 1)
      location = point_for(prop, x, y)
      owner = owner_of(location, prop)
      Limelight::UI::Events::MouseExitedEvent.new(owner, modifiers, location, click_count).dispatch(owner)
    end

    def wheel(prop, scroll_amount = 1, x = 0, y = 0, modifiers = 0, click_count = 0, scroll_type = 0, wheel_rotation = 1)
      location = point_for(prop, x, y)
      owner = owner_of(location, prop)
      Limelight::UI::Events::MouseWheelEvent.new(owner, modifiers, location, click_count, scroll_type, scroll_amount, wheel_rotation).dispatch(owner)
    end

    private

    def point_for(prop, x, y)
      absolute_location = prop.panel.absolute_location
      local_x = absolute_location.x + x
      local_y = absolute_location.y + y
      return Java::java.awt.Point.new(local_x, local_y)
    end

    def owner_of(location, prop)      
      owner = prop.panel.get_owner_of_point(location)
      return owner
    end

  end

end