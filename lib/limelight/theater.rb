#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/limelight_exception'

module Limelight
  
  class Theater
    
    include Rapi::Theater
    
    attr_reader :active_stage
    
    def initialize
      @stages = {}
    end
    
    def stages
      return @stages.values
    end
    
    def [](stage_name)
      return @stages[stage_name]
    end
    
    def add_stage(stage)
      raise LimelightException.new("Duplicate stage name: '#{stage.name}'") if @stages[stage.name]
      @stages[stage.name] = stage
    end
    
    def stage_activated(stage)
      @active_stage = stage
    end
    
    def default_stage
      add_stage(Stage.new(self, "Limelight")) if self["Limelight"].nil?
      return self["Limelight"]
    end
    
  end
  
end