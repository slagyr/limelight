#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/stage'
require 'limelight/limelight_exception'

module Limelight
  
  def self.build_stages(theater, &block)
    builder = StagesBuilder.new(theater)
    builder.instance_eval(&block) if block
    return builder.__stages__
  end
  
  class StagesBuilder

    Limelight::Util.lobotomize(self)

    attr_reader :__stages__
    
    def initialize(theater)
      @__theater__ = theater
      @__stages__ = []
    end
    
    def stage(name, &block)
      stage_builder = StageBuilder.new(@__theater__, name)
      stage_builder.instance_eval(&block) if block
      @__stages__ << stage_builder.__stage__
    end
    
  end
  
  class StageBuilder

    Limelight::Util.lobotomize(self)
    
    attr_reader :__stage__
    
    def initialize(theater, name)
      if theater[name]
        @__stage__ = theater[name]
      else
        @__stage__ = Stage.new(theater, name)
        theater.add_stage(@__stage__)
      end
    end
    
    def default_scene(scene_name)
      @__stage__.default_scene = scene_name
    end
    
    def method_missing(sym, value)
      setter_sym = "#{sym}=".to_s
      raise StageBuilderException.new(sym) if !@__stage__.respond_to?(setter_sym)
      @__stage__.send(setter_sym, value)
    end
  end

  class StageBuilderException < LimelightException
    def initialize(name)
      super("'#{name}' is not a valid stage property")
    end
  end
  
end