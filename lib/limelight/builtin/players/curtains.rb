module Limelight
  module Builtin
    module Players

      module Curtains

        def open
          scene.remove(self)
        end

        def mouse_clicked(e)
          open
        end

      end

    end
  end
end