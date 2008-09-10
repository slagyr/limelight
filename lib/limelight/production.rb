#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/limelight_exception'

module Limelight
  
  class Production
    
    class << self
      
      def index(production)
        @index = [] if @index.nil?
        if production.name.nil?
          assign_name_to(production) 
        else
          error_if_duplicate_name(production.name)
        end
        @index << production
      end
      
      def [](name)
        return nil if @index.nil?
        return @index.find { |production| production.name == name }
      end
      
      def assign_name_to(production)
        count = @index.length + 1
        while name_taken?(count.to_s)
          count += 1
        end
        production.name = count.to_s
      end
      
      def name_taken?(name)
        return self[name] != nil
      end

      def clear_index
        @index = []
      end
      
      def error_if_duplicate_name(name)
        raise Limelight::LimelightException.new("Production name '#{name}' is already taken") if name_taken?(name)
      end
      
    end
    
    attr_reader :producer, :theater, :name
    
    def initialize(producer, theater)
      @producer = producer
      @theater = theater
      self.class.index(self)
    end
    
    def name=(value)
      self.class.error_if_duplicate_name(value)
      @name = value
    end
    
  end
  
end