#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/limelight_exception'
require 'limelight/file_loader'
require 'limelight/studio'
require 'drb'

module Limelight

  # The root object of Limelight Production.  Every Prop in a production has access to its Production object.
  # Therefore it is typical to store reasources in the Production.
  #
  # Productions are configured, and attributes are added, by the ProductionBuilder.
  #
  class Production

    class << self

      def [](name) #:nodoc:
        return Studio[name]
      end

    end

    attr_reader :name, :root
    attr_accessor :producer, :theater

    # Users typically need not create Production objects.
    #
    def initialize(path)
      @root = FileLoader.for_root(path)
    end

    # Sets the name of the Production.  The name must be unique amongst all Productions in memory.
    #
    def name=(value)
      Studio.error_if_duplicate_name(value)
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

    # Returns the path to the production's production.rb file
    #
    def production_file
      return @root.path_to("production.rb")
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
    
    # Returns true if the production allows itself to be closed.  The system will call this methods when
    # it wishes to close the production, perhaps when the user quits the application.  By default the production
    # will always return true.
    #
    def allow_close?
      return true
    end

    # Called when the production is about to be opened.  The default implementation does nothing but you may re-implement
    # it in the production.rb file.
    #
    def production_opening
    end

    # Called when the production has been loaded.  That is, when all the gems have been loaded stages have been
    # instantiated.
    #
    def production_loaded
    end

    # Called when the production is fully opened.  The default implementation does nothing but you may re-implement
    # it in the production.rb file.
    #
    def production_opened
    end

    # Called when the production is about to be closed.  The default implementation does nothing but you may re-implement
    # it in the production.rb file.
    #
    def production_closing
    end

    # Called when the production is fully closed.  The default implementation does nothing but you may re-implement
    # it in the production.rb file.
    #
    def production_closed
    end

    # Closes the production. If there are no more productions open, the Limelight runtime will shutdown.
    #
    def close
      self.production_closing
      Studio.production_closed(self)
      self.production_closed
    end

  end

end