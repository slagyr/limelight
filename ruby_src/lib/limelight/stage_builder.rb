require 'limelight/stage'

module Limelight
  
  def self.build_stage(producer, &prop)
    builder = StageBuilder.new(producer)
    builder.instance_eval(&prop) if prop
    return builder.stage
  end
  
  class StageBuilder
    
    attr_reader :stage
    
    def initialize(producer)
      @stage = Stage.new(producer)
    end
    
    def default_scene(scene_name)
      @stage.default_scene = scene_name
    end
    
  end
  
end