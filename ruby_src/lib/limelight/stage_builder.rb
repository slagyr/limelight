require 'limelight/stage'

module Limelight
  
  def self.build_stages(producer, &block)
    builder = StagesBuilder.new(producer)
    builder.instance_eval(&block) if block
    return builder.stages
  end
  
  class StagesBuilder
    
    attr_reader :stages
    
    def initialize(producer)
      @producer = producer
      @stages = []
    end
    
    def stage(name, &block)
      stage_builder = StageBuilder.new(@producer, name)
      stage_builder.instance_eval(&block) if block
      @stages << stage_builder.stage
    end
    
  end
  
  class StageBuilder
    
    attr_reader :stage
    
    def initialize(producer, name)
      if producer.theater[name]
        @stage = producer.theater[name]
      else
        @stage = Stage.new(producer, name)
        producer.theater.add_stage(@stage)
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

  class StageBuilderException < Exception
    def initialize(name)
      super("'#{name}' is not a valid stage property")
    end
  end
  
end