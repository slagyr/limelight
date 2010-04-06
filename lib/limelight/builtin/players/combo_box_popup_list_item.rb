#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Builtin
    module Players

      module ComboBoxPopupListItem #:nodoc:

        attr_accessor :combo_box

        def mouse_clicked(e)
          @combo_box.value = text
          parent.close
        end

      end

    end
  end
end