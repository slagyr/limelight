#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/stage'
require 'limelight/limelight_exception'

module Limelight

  # A trigger to build/configure Stage objects using the StageBuilder DSL.
  #
  # See Limelight::Stagesbuilder, Limelight::Stagebuilder
  #
  def self.build_stages(theater, &block)
    builder = DSL::StagesBuilder.new(theater)
    builder.instance_eval(&block) if block
    return builder.__stages__
  end

  module DSL

    # The basis of the DSL for building Stage objects.
    #
    #  stage "inspector" do
    #    default_scene "inspector"
    #    title "Limelight Composer Inspector"
    #    location [0, 0]
    #    size [300, 800]
    #  end
    #
    #  stage "viewer" do
    #    title "Limelight Composer Viewer"
    #    location [350, 0]
    #    size [800, 800]
    #  end
    #
    # In this example above, two stages are created for the production.  One is named 'inspector' and the other is named
    # 'viewer'.  'inspector' has a default scene that will be loaded on startup.  'viewer' will not contain any scene
    # on startup.  Using the DSL, each stage can be configured to set the title, location, size, or any other attribute
    # of a stage.
    #
    # See Limelight::Stage
    #
    class StagesBuilder

      Limelight::Util.lobotomize(self)

      attr_reader :__stages__

      # Constructs a new StagesBuilder.  A Theater object is required as a parameter.  Each stages created will belong
      # to the Theater passed in.
      #
      def initialize(theater)
        @__theater__ = theater
        @__stages__ = []
      end

      # Creates a new stage with the provided name.  The block will contain StageBuilder DSL to configure the stage.
      #
      def stage(name, &block)
        stage_builder = StageBuilder.new(@__theater__, name)
        stage_builder.instance_eval(&block) if block
        @__stages__ << stage_builder.__stage__
      end

    end

    # The basis of the DSL for configuring a Stage object.
    #
    class StageBuilder

      Limelight::Util.lobotomize(self)

      attr_reader :__stage__

      def initialize(theater, name) #:nodoc:
        if theater[name]
          @__stage__ = theater[name]
        else
          @__stage__ = theater.add_stage(name)
        end
      end

      # Specifies the scene that will be displayed on the stage upon opening of the production.
      #
      def default_scene(scene_name)
        @__stage__.default_scene = scene_name
      end

      def method_missing(sym, *values)  #:nodoc:     
        setter_sym = "#{sym}=".to_s
        raise StageBuilderException.new(sym) if !@__stage__.respond_to?(setter_sym)
        @__stage__.send(setter_sym, *values)
      end
    end

    # Exception thrown by StageBuilder in the event of an invalid configuration.
    #
    class StageBuilderException < LimelightException
      def initialize(name)
        super("'#{name}' is not a valid stage property")
      end
    end

  end
end