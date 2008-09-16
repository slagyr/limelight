#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

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

class String

  # Converts Ruby style names to Java style camal case.
  #
  #   "four_score".camalized # => "FourScore"
  #   "and_seven_years".camalized(:lower) # => "andSevenYears"
  #
  def camalized(starting_case = :upper)
    value = self.downcase.gsub(/[_| ]([a-z])/) { |match| match[-1..-1].upcase } 
    value = value[0..0].upcase + value[1..-1] if starting_case == :upper
    return value
  end
  
end
  
