#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

module Limelight

  class Keyboard
    def press(key, prop, key_location=1, x=0, y=0, modifiers=0)
      location = point_for(prop, x, y)
      owner = owner_of(location, prop)
      Java::limelight.ui.events.panel.KeyPressedEvent.new(modifiers, key, key_location).dispatch(owner)
    end

    def release(key, prop, key_location=1, x=0, y=0, modifiers=0)
      location = point_for(prop, x, y)
      owner = owner_of(location, prop)
      Java::limelight.ui.events.panel.KeyReleasedEvent.new(modifiers, key, key_location).dispatch(owner)
    end

    private 

    def point_for(prop, x, y)
      absolute_location = prop.peer.absolute_location
      local_x = absolute_location.x + x
      local_y = absolute_location.y + y
      return Java::java.awt.Point.new(local_x, local_y)
    end

    def owner_of(location, prop)
      owner = prop.peer.get_owner_of_point(location)
      return owner
    end

  end

end
