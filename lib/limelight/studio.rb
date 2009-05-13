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

      # Class level methods to return the one instance of Studio
      #
      def instance
        @studio = self.new if @studio.nil?
        return @studio
      end

    end

    attr_reader :productions

    def initialize
      @productions = []
    end

    # Opens the production at the specified path.
    #
    def open(production_path)
      producer = Producer.new(production_path)
      producer.open
      production = producer.production
      production.studio = self
      @productions << production
    end

    # Returns true if all of the open productions allow closing.
    #
    def should_allow_shutdown
      return @productions.all? { |production| production.allow_close? }
    end

    # Called when a production is closed to notify the studio of the event.
    #
    def production_closed(production)
      @productions.delete(production)

      if(@productions.empty?)
        Thread.new { Context.instance().shutdown }
      end 
    end
  end

end