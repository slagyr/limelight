require 'java'
require File.expand_path(File.dirname(__FILE__) + "/../../../dist/limelight.jar")

module Limelight
  # JBlock = Java::limelight.Block
  # JPage = Java::limelight.Page
  JBook = Java::limelight.Book
  Style = Java::limelight.Style
end

class Class
  
  def getters(*symbols)
    symbols.each do |symbol|
      captalized_name = symbol.to_s[0...1].upcase + symbol.to_s[1..-1]
      self.class_eval "def get#{captalized_name}; return @#{symbol}; end"
    end
  end
  
  def setters(*symbols)
    symbols.each do |symbol|
      captalized_name = symbol.to_s[0...1].upcase + symbol.to_s[1..-1]
      self.class_eval "def set#{captalized_name}(value); @#{symbol} = value; end"
    end
  end
  
end
  
