#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

require 'limelight/limelight_exception'
require 'thread'

module Limelight

  # A Theater represents a group of Stages.  Productions require a Theater in which to open.
  #
  class Theater

    include Java::limelight.model.api.TheaterProxy

    attr_reader :peer #:nodoc:

    def initialize(production, real_theater)
      @production = production
      @peer = real_theater
      @peer.proxy = self
    end

    # Returns the theater's active stage.  i.e. the stage most recently used.
    #
    def active_stage
      @peer.active_stage
    end

    # Returns an Array of Stages that belong to this Theater.
    #
    def stages
      stages = []
      @peer.stages.each { |stage| stages << stage.proxy }
      return stages
    end

    # Returns true if the theater has at least one stage
    #
    def has_stages?
      return @peer.has_stages?
    end

    # Returns the Stage with the spcified name, nil if none exist with the specified name.
    #
    def [](stage_name)
      stage = @peer.get(stage_name)
      return stage == nil ? nil : stage.proxy
    end

    # Adds a Stage to the Theater.  Raises an exception is the name of the Stage is duplicated.
    #
    def add_stage(name, options = {})
      stage = build_stage(name, options).proxy
      @peer.add(stage.peer)
      return stage
    end

    # Close this theater.  All stages in this theater will be closed and the active_stage will be nil'ed.
    #
    def close
      @peer.close
    end

    #TODO MDM - This ia bug waiting to happen.  Need to return the Java peer from this method.
    def build_stage(name, options) #:nodoc:
      return Limelight::Stage.new(self, name, options).peer
    end
    alias :buildStage :build_stage

    def default_stage
      @peer.default_stage.proxy
    end

  end

end