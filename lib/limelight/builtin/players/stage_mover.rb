#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Builtin
    module Players

      module StageMover

        def mouse_pressed(e)        
          @drag_reference_x = e.x
          @drag_reference_y = e.y
        end

        def mouse_dragged(e)
          dx = e.x - @drag_reference_x
          dy = e.y - @drag_reference_y 

          frame = scene.stage.frame
          location = frame.location

          location.x += dx
          location.y += dy
          frame.location = location
        end

      end

    end
  end
end