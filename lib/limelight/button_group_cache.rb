module Limelight
  ButtonGroupCache = Java::limelight.ui.ButtonGroupCache
  class ButtonGroupCache
    def [](key)
      return self.get(key)
    end
  end
end
