#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  ButtonGroupCache = UI::ButtonGroupCache #:nodoc:
  class ButtonGroupCache #:nodoc:
    def [](key)
      return self.get(key)
    end
  end
end
