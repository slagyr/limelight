#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/limelight_exception'

module Limelight

  # A Theater represents a group of Stages.  Productions require a Theater in which to open.
  #
  class Theater
    
    include UI::Api::Theater

    # Returns the theater's active stage.  i.e. the stage most recently used.
    #
    attr_reader :active_stage
    
    def initialize(production)
      @production = production
      @stages = {}
    end

    # Returns an Array of Stages that belong to this Theater.
    #
    def stages
      return @stages.values
    end

    # Returns true if the theater has at least one stage
    #
    def has_stages?
      return stages.length > 0
    end

    # Returns the Stage with the spcified name, nil if none exist with the specified name.
    #
    def [](stage_name)
      return @stages[stage_name]
    end

    # Adds a Stage to the Theater.  Raises an exception is the name of the Stage is duplicated.
    #
    def add_stage(name, options = {})
      raise LimelightException.new("Duplicate stage name: '#{name}'") if @stages[name]
      stage = build_stage(name, options)
      @stages[name] = stage
      return stage
    end

    # Lets the Theater know which stage has the limelight (currently active).
    #
    def stage_activated(stage)
      @active_stage = stage
    end

    # Removes the stage from this theater.
    #
    def stage_closed(stage)
      @stages.delete(stage.name)
      @active_stage = nil if @active_stage == stage
      @production.theater_empty! if @stages.empty?
    end

    # If no Stages are added, the Theater will provide a default Stage named "Limelight".
    #
    def default_stage
      add_stage("Limelight") if self["Limelight"].nil?
      return self["Limelight"]
    end

    # Close this theater.  All stages in this theater will be closed and the active_stage will be nil'ed.
    #
    def close
      @stages.values.each { |stage| stage.close }
      @stages.clear
      @active_stage = nil
    end

    protected #############################################

    def build_stage(name, options)
      return Limelight::Stage.new(self, name, options)
    end
    
  end
  
end