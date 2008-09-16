#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Builtin
    module Players

      module ComboBoxPopupList #:nodoc:

        attr_accessor :curtains

        def close
          @curtains.open
        end

      end
    end
  end
end