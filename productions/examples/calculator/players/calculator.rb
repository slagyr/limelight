require 'calculator_model'
module Calculator
  
  attr_accessor :calculator_model
  
  def extended
    p self
    @calculator_model = ::CalculatorModel.new
  end
  
  
end