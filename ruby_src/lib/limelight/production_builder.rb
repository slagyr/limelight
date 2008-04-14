require 'limelight/production'
require 'limelight/limelight_exception'

module Limelight
  
  def self.build_production(producer, theater, &block)
    builder = ProductionBuilder.new(producer, theater)
    builder.instance_eval(&block) if block
    return builder.__production__
  end
  
  class ProductionBuilder
    
    class << self
      
      attr_accessor :current_attribute
      
    end
    
    attr_reader :__production__
    
    def initialize(producer, theater)
      @__production__ = Production.new(producer, theater)
    end
    
    def method_missing(sym, value)
      setter_sym = "#{sym}=".to_s
      raise ProductionBuilderException.new(sym) if !@__production__.respond_to?(setter_sym)
      @__production__.send(setter_sym, value)
    end
    
    def attribute(sym)
      ProductionBuilder.current_attribute = sym
      class << @__production__
        attr_accessor ProductionBuilder.current_attribute
      end
    end
  end
  
  class ProductionBuilderException < LimelightException
    def initialize(name)
      super("'#{name}' is not a valid production property")
    end
  end
  
end