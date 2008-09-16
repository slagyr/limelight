#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/limelight_exception'

module Limelight

  # A Theater represents a group of Stages.  Productions require a Theater in which to open.
  #
  class Theater
    
    include UI::Api::Theater
    
    attr_reader :active_stage
    
    def initialize
      @__stages__ = {}
    end

    # Returns an Array of Stages that belong to this Theater.
    #
    def stages
      return @__stages__.values
    end

    # Returns the Stage with the spcified name, nil if none exist with the specified name.
    #
    def [](stage_name)
      return @__stages__[stage_name]
    end

    # Adds a Stage to the Theater.  Raises an exception is the name of the Stage is duplicated.
    #
    def add_stage(stage)
      raise LimelightException.new("Duplicate stage name: '#{stage.name}'") if @__stages__[stage.name]
      @__stages__[stage.name] = stage
    end

    # Lets the Theater know which stage has the limelight (currently active).
    #
    def stage_activated(stage)
      @active_stage = stage
    end

    # If no Stages are added, the Theater will provide a default Stage named "Limelight".
    #
    def default_stage
      add_stage(Stage.new(self, "Limelight")) if self["Limelight"].nil?
      return self["Limelight"]
    end
    
  end
  
end