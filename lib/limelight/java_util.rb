#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/string'

class Class

  # Class level method to creates Java style getters.
  #
  #   getters :foo, :bar
  #
  # Creates the following methods:
  #
  #   def getFoo
  #     return @foo
  #   end
  #
  #   def getBar
  #     return @bar
  #   end
  #
  def getters(*symbols)
    symbols.each do |symbol|
      self.class_eval "def get#{symbol.to_s.camalized}; return #{symbol}; end"
    end
  end

  # Class level method to creates Java style setters.
  #
  #   setters :foo, :bar
  #
  # Creates the following methods:
  #
  #   def setFoo(value)
  #     @foo = value
  #   end
  #
  #   def setBar(value)
  #     @bar = value
  #   end
  #
  def setters(*symbols)
    symbols.each do |symbol|
      self.class_eval "def set#{symbol.to_s.camalized}(value); self.#{symbol} = value; end"
    end
  end
  
end
  
