#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/production'
require 'limelight/limelight_exception'
require 'limelight/util'

module Limelight

  # A trigger to configure Production objects using the ProductionBuilder DSL.
  #
  # See Limelight::ProductionBuilder
  #
  def self.build_production(production, &block)
    builder = DSL::ProductionBuilder.new(production)
    builder.instance_eval(&block) if block
    return builder.__production__
  end

  module DSL
    # The basis of the DSL for building Style objects.
    #
    #  name "Stage Composer"
    #  attribute :controller
    #  attribute :inspector
    #
    # The above example names the Production 'Stage Composer' and creates two attributes on the Production: 'controller'
    # and 'inspector'
    #
    class ProductionBuilder
      
      Limelight::Util.lobotomize(self)

      class << self

        attr_accessor :current_attribute

      end

      attr_reader :__production__

      def initialize(production)
        @__production__ = production
      end

      def method_missing(sym, value) #:nodoc:
        setter_sym = "#{sym}=".to_s
        raise ProductionBuilderException.new(sym) if !@__production__.respond_to?(setter_sym)
        @__production__.send(setter_sym, value)
      end

      # Creates an attribute on the Production
      #
      def attribute(sym)
        ProductionBuilder.current_attribute = sym
        class << @__production__
          attr_accessor ProductionBuilder.current_attribute
        end
      end
    end

    # Thrown if there is an error in the ProductionBuilder DSL
    #
    class ProductionBuilderException < LimelightException
      def initialize(name)
        super("'#{name}' is not a valid production property")
      end
    end

  end
end