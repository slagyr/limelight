#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/limelight_exception'

module Limelight

  # The root object of Limelight Production.  Every Prop in a production has access to its Production object.
  # Therefore it is typical to store reasources in the Production.
  #
  # Productions are configured, and attributes are added, by the ProductionBuilder.
  #
  class Production
    
    class << self
      
      def index(production) #:nodoc:
        @index = [] if @index.nil?
        if production.name.nil?
          assign_name_to(production) 
        else
          error_if_duplicate_name(production.name)
        end
        @index << production
      end
      
      def [](name) #:nodoc:
        return nil if @index.nil?
        return @index.find { |production| production.name == name }
      end
      
      def assign_name_to(production) #:nodoc:
        count = @index.length + 1
        while name_taken?(count.to_s)
          count += 1
        end
        production.name = count.to_s
      end
      
      def name_taken?(name) #:nodoc:
        return self[name] != nil
      end

      def clear_index #:nodoc:
        @index = []
      end
      
      def error_if_duplicate_name(name) #:nodoc:
        raise Limelight::LimelightException.new("Production name '#{name}' is already taken") if name_taken?(name)
      end
      
    end
    
    attr_reader :path, :producer, :theater, :name

    # Users typically need not create Production objects.
    #
    def initialize(path, producer, theater)
      @path = path
      @producer = producer
      @theater = theater
      self.class.index(self)
    end

    # Sets the name of the Production.  The name must be unique amongst all Productions in memory.
    #
    def name=(value)
      self.class.error_if_duplicate_name(value)
      @name = value
    end
    
  end
  
end