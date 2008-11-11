#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Builtin
    module Players

      module Curtains #:nodoc:

        def open
puts "Curtains.open"          
          scene.remove(self)
        end

        def mouse_clicked(e)
          open
        end

      end

    end
  end
end