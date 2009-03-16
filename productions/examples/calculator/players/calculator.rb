#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'calculator_model'
module Calculator
  
  attr_accessor :calculator_model
  
  def extended
    p self
    @calculator_model = ::CalculatorModel.new
  end
  
  
end