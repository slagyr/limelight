#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/stage'
require 'limelight/limelight_exception'

module Limelight
  
  def self.build_stages(theater, &block)
    builder = StagesBuilder.new(theater)
    builder.instance_eval(&block) if block
    return builder.stages
  end
  
  class StagesBuilder
    
    attr_reader :stages
    
    def initialize(theater)
      @theater = theater
      @stages = []
    end
    
    def stage(name, &block)
      stage_builder = StageBuilder.new(@theater, name)
      stage_builder.instance_eval(&block) if block
      @stages << stage_builder.stage
    end
    
  end
  
  class StageBuilder
    
    attr_reader :stage
    
    def initialize(theater, name)
      if theater[name]
        @stage = theater[name]
      else
        @stage = Stage.new(theater, name)
        theater.add_stage(@stage)
      end
    end
    
    def default_scene(scene_name)
      @stage.default_scene = scene_name
    end
    
    def method_missing(sym, value)
      setter_sym = "#{sym}=".to_s
      raise StageBuilderException.new(sym) if !@stage.respond_to?(setter_sym)
      @stage.send(setter_sym, value)
    end
  end

  class StageBuilderException < LimelightException
    def initialize(name)
      super("'#{name}' is not a valid stage property")
    end
  end
  
end