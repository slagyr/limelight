#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Builtin
    module Players

      module StageSizer

        def mouse_pressed(e)
          @sizer_reference_x = e.x
          @sizer_reference_y = e.y
          @sizer_frame = scene.stage.frame
          @sizer_reference_size = @sizer_frame.size
        end

        def mouse_dragged(e)
          return unless @sizer_frame
          dx = e.x - @sizer_reference_x
          dy = e.y - @sizer_reference_y

          @sizer_reference_x = e.x
          @sizer_reference_y = e.y

          @sizer_reference_size.width += dx
          @sizer_reference_size.height += dy
          @sizer_frame.size = @sizer_reference_size
        end

      end

    end
  end
end