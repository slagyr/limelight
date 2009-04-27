#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/limelight_exception'
require 'limelight/file_loader'
require 'drb'

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

    attr_reader :name, :root
    attr_accessor :producer, :theater

    # Users typically need not create Production objects.
    #
    def initialize(path)
      @root = FileLoader.for_root(path)
      self.class.index(self)
    end

    # Sets the name of the Production.  The name must be unique amongst all Productions in memory.
    #
    def name=(value)
      self.class.error_if_duplicate_name(value)
      @name = value
    end

    # Return the path to the root directory of the production
    #
    def path
      return @root.root
    end

    # Returns the path to the production's init file
    #
    def init_file
      return @root.path_to("init.rb")
    end

    # Returns the path to the production's stages file
    #
    def stages_file
      return @root.path_to("stages.rb")
    end

    # Returns the path to the production's styles file
    #
    def styles_file
      return @root.path_to("styles.rb")
    end

    # Returns the path to the production's gems directory
    #
    def gems_directory
      return @root.path_to("__resources/gems")
    end

    # Returns the path to the named Scene's directory within the Production
    #
    def scene_directory(name)
      return @root.root if name == :root
      return @root.path_to(name)
    end

    # Closes the production and shuts down the Limelight runtime.
    #
    def close
      Thread.new { Context.instance().shutdown }
    end

    # Publish the production, using DRb, on the specified port.
    #
    def publish_on_drb(port)
      @drb_server = DRb.start_service("druby://0.0.0.0:#{port}", self)      
    end

  end

end