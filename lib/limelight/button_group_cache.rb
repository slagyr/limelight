#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  ButtonGroupCache = Java::limelight.ui.ButtonGroupCache
  class ButtonGroupCache
    def [](key)
      return self.get(key)
    end
  end
end
