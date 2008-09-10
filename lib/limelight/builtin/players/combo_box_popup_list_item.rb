module Limelight
  module Builtin
    module Players

      module ComboBoxPopupListItem

        attr_accessor :combo_box

        def mouse_clicked(e)
          @combo_box.value = text
          parent.close
        end

      end

    end
  end
end