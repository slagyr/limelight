#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/limelight_exception'
require 'thread'

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
      @mutex = Mutex.new
    end

    # Returns an Array of Stages that belong to this Theater.
    #
    def stages
      return @mutex.synchronize { @stages.values }
    end

    # Returns true if the theater has at least one stage
    #
    def has_stages?
      return stages.length > 0
    end

    # Returns the Stage with the spcified name, nil if none exist with the specified name.
    #
    def [](stage_name)
      return @mutex.synchronize { @stages[stage_name] }
    end

    # Adds a Stage to the Theater.  Raises an exception is the name of the Stage is duplicated.
    #
    def add_stage(name, options = {})
      raise LimelightException.new("Duplicate stage name: '#{name}'") if @stages[name]
      stage = build_stage(name, options)
      @mutex.synchronize { @stages[name] = stage }
      return stage
    end

    # Invoked when a stage, blonging to this theater becomes active.  Lets the Theater know which stage
    # has the limelight (currently active).  Only 1 stage may be active at a time.
    #
    def stage_activated(stage)
      @active_stage = stage
    end

    # Invoked when a stage, belonging to this theater, loose it's status as the active stage. The active_stage is
    # cleared.  Only 1 stage may be active at a time.
    #
    def stage_deactivated(stage)
      @active_stage = nil
      @production.theater_empty! if !any_visible_stages?
    end

    # Removes the stage from this theater.
    #
    def stage_closed(stage)
      @mutex.synchronize { @stages.delete(stage.name) }
      @active_stage = nil if @active_stage == stage
      @production.theater_empty! if !any_stages? || !any_visible_stages?
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
      stages_to_close = @mutex.synchronize { @stages.values.dup }
      stages_to_close.each { |stage| stage.close }
      @mutex.synchronize { @stages.clear }
      @active_stage = nil
    end

    protected #############################################

    def build_stage(name, options)
      return Limelight::Stage.new(self, name, options)
    end

    private ###############################################

    def any_stages?
      return @mutex.synchronize { @stages.empty? }
    end

    def any_visible_stages?
     return @mutex.synchronize { @stages.values.any? { |s| s.visible? } }  
    end
    
  end
  
end