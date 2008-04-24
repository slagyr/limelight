class Class
  
  def getters(*symbols)
    symbols.each do |symbol|
      self.class_eval "def get#{symbol.to_s.camalized}; return #{symbol}; end"
    end
  end
  
  def setters(*symbols)
    symbols.each do |symbol|
      self.class_eval "def set#{symbol.to_s.camalized}(value); self.#{symbol} = value; end"
    end
  end
  
end

class String
  
  def camalized(starting_case = :upper)
    value = self.downcase.gsub(/[_| ]([a-z])/) { |match| match[-1..-1].upcase } 
    value = value[0..0].upcase + value[1..-1] if starting_case == :upper
    return value
  end
  
end
  
