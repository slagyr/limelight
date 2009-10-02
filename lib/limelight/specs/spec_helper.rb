#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'spec'
require File.expand_path(File.dirname(__FILE__) + "/../../init")
require 'limelight/scene'
require 'limelight/producer'
require 'limelight/string'

module Limelight
  module Specs

    class << self
      attr_accessor :producer
    end

    module SpecHelper

      def open_scene
        if @ll_spec_options[:stage]
          stage = producer.theater[@ll_spec_options[:stage]]
          raise "No such stage: '#{@ll_spec_options[:stage]}'" unless stage
        else
          stage = producer.theater.default_stage
        end

        stage.should_remain_hidden = @ll_spec_options[:hidden] || true

        @scene = producer.open_scene(@scene_name.to_s, stage)
      end

      def load_player
        @scene = MockScene.new
        @player = Limelight::Prop.new
        @scene << @player
        @player.extend(eval(@player_name.to_s.camalized))
      end

      def player
        load_player unless @player
        return @player
      end

      def scene
        open_scene unless @scene
        return @scene
      end

    end
    
    class MockScene < Limelight::Scene
      def method_missing(meth, *args, &blk)
        method_string = meth.to_s
        if method_string =~ /=$/
          MockScene.class_eval <<-EOF
            attr_accessor method_string[0..-2]
EOF
          send(meth, *args, &blk)
        else
          super(meth, args, &blk)
        end
      end
    end
  end
end

module Spec
  module Example
    class ExampleGroup

      def self.uses_scene(scene_name, optixons = {})
        include Limelight::Specs::SpecHelper

        before(:each) do
          @scene_name = scene_name
          @ll_spec_options = options
          @scene = nil
        end
      end

      def self.uses_player(player_name)
        include Limelight::Specs::SpecHelper

        before(:each) do
          @player_name = player_name
          load_player
        end
      end

      after(:suite) do
        unless Limelight::Specs.producer.nil?
          Limelight::Specs.producer.theater.stages.each do |stage|
            # MDM - We do this in a round-about way to reduce the chance of using stubbed or mocked methods.
            frame = stage.instance_variable_get("@frame")
            frame.close if frame
          end
        end
      end

      def producer
        if Limelight::Specs.producer.nil?
          if $with_ui
            Limelight::Main.initializeContext
          else
            Limelight::Main.initializeTestContext
          end
          raise "$PRODUCTION_PATH undefined.  Make sure you specify the location of the production in $PRODUCTION_PATH." unless defined?($PRODUCTION_PATH)
          raise "Could not find production: '#{$PRODUCTION_PATH}'. Check $PRODUCTION_PATH." unless File.exists?($PRODUCTION_PATH)
          Limelight::Specs.producer = Limelight::Producer.new($PRODUCTION_PATH)
          Limelight::Specs.producer.load
        end
        return Limelight::Specs.producer
      end

      def production
        return producer.production
      end

    end
  end
end