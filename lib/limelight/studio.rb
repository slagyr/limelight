#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/producer'

module Limelight

  # A Studio creates Productions.  There is only one instance of Studio per Limelight runtime.  All open productions
  # are opened by the studio.
  #
  class Studio

    class << self

      def install #:nodoc:
        Context.instance.studio = instance
        return instance
      end

      def instance #:nodoc:
        @studio = self.new if @studio.nil?
        return @studio
      end

      def reset #:nodoc:
        @studio = nil
      end

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

    def error_if_duplicate_name(name) #:nodoc:
      raise Limelight::LimelightException.new("Production name '#{name}' is already taken") if name_taken?(name)
    end

    # Opens the production at the specified path.
    #
    def open(production_path)
      begin
        src = <<END
#begin
  puts "$:: \#{$:.join(", ")}"
  puts "__FILE__: \#{__FILE__}"
  require 'limelight/limelight_init'
  require 'limelight/producer'
  producer = Limelight::Producer.new("#{production_path}")
  production = producer.production
  Java::java.lang.Thread.currentThread.handle = production
  producer.open
#rescue Exception => e
#  raise e
#end
END
        production = Context.instance().runtimeFactory.spawn(src)
        index(production)
        return production
      rescue StandardError => e
        alert_and_shutdown(e)
      end
    end

#      def open(production_path)
#        begin
#          producer = Producer.new(production_path)
#          production = producer.production
#          index(production)
#          producer.open
#          return production
#        rescue StandardError => e
#          alert_and_shutdown(e)
#        end
#      end

    # Returns true if all of the open productions allow closing.
    #
    def should_allow_shutdown
      return true if @index.nil?
      return @index.all? { |production| production.allow_close? }
    end

    # If allowed (should_allow_shutdown), this will close all open productions and shutdown the limelight runtime.
    #
    def shutdown
      return if @is_shutdown || @is_shutting_down
      return unless should_allow_shutdown
      @is_shutting_down = true
      @index.each { |production| production.close } if @index
      @utilities_production.close if @utilities_production
      @is_shutdown = true
      Thread.new { Context.instance().shutdown }
    end

    # Called when a production is closed to notify the studio of the event.
    #
    def production_closed(production)
      puts "Studio.production_closed @index: #{@index}"
      if @index
        @index.delete(production)
        puts "closing production: #{production}"
production.audit        
        Context.instance().runtimeFactory.terminate(production)
puts "index.inspect: #{@index.inspect}"        
      end
      shutdown if @index && @index.empty?
    end

    # Returns an array of all the productions
    #
    def productions
      return @index.nil? ? [] : @index.dup
    end

    # Publish the Studio, using DRb, on the specified port.
    #
    def publish_on_drb(port)
      @drb_server = DRb.start_service("druby://0.0.0.0:#{port}", self)
    end

    # Returns the utilities production; a production used by limelight.
    #
    def utilities_production
      if @utilities_production == nil
        producer = Producer.new(File.join($LIMELIGHT_LIB, "limelight", "builtin", "utilities_production"))
        @utilities_production = producer.production
        producer.open
      end
      return @utilities_production
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

    def alert_and_shutdown(e)
      begin
        message = "#{e}\n#{e.backtrace.join("\n")}"
        utilities_production.alert(message)
        shutdown if @index.nil? || @index.empty?
      rescue Exception => e
        puts e
        puts e.backtrace
      end
    end

  end

end