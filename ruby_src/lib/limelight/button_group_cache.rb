module Limelight
  ButtonGroupCache = Java::limelight.ButtonGroupCache
  class ButtonGroupCache
    def [](key)
      return self.get(key)
    end
  end
end
