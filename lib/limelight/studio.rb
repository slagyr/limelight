#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/producer'

module Limelight

  # A Studio creates Productions.  There is only one instance of Studio per Limelight runtime.  All open productions
  # are opened by the studio.
  #
  class Studio

    include Limelight::UI::Api::Studio

    class << self

      def install #:nodoc:
        Context.instance.studio = instance
      end

      def instance #:nodoc:
        @studio = self.new if @studio.nil?
        return @studio
      end

      def index(production) #:nodoc:
        @index = [] if @index.nil?
        if name_taken?(production.name)
          assign_unique_name(production)
        elsif production.name.nil?
          assign_name_to(production)
        else
          error_if_duplicate_name(production.name)
        end
        @index << production
      end

      # Returns the production with the specified name that was previously opened but the Studio.
      #
      def [](name)
        return nil if @index.nil?
        return @index.find { |production| production.name == name }
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

      # Opens the production at the specified path.
      #
      def open(production_path)
        producer = Producer.new(production_path)
        production = producer.production
        index(production)
        producer.open
      end

      # Returns true if all of the open productions allow closing.
      #
      def should_allow_shutdown
        return true if @index.nil?
        return @index.all? { |production| production.allow_close? }
      end

      # Called when a production is closed to notify the studio of the event.
      #
      def production_closed(production)
        @index.delete(production)

        if (@index.empty?)
          Thread.new { Context.instance().shutdown }
        end
      end

      # Returns an array of all the productions
      #
      def productions
        return @index.nil? ? [] : @index.dup
      end

      private #############################################

      def assign_unique_name(production) #:nodoc:
        count = 2
        name = production.name
        while name_taken?(name)
          name = "#{production.name}_#{count}"
          count += 1
        end  
        production.name = name
      end

      def assign_name_to(production) #:nodoc:
        count = @index.length + 1
        while name_taken?(count.to_s)
          count += 1
        end
        production.name = count.to_s
      end

    end

    # Same as the class level method.
    #
    def open(production_path)
      self.class.open(production_path)
    end

    # Same as the class level method.
    #
    def should_allow_shutdown
      return self.class.should_allow_shutdown
    end
  end

end