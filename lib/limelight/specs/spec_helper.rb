#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'spec'
require File.expand_path(File.dirname(__FILE__) + "/../../init")
require 'limelight/scene'
require 'limelight/producer'

$producer = nil
$with_ui = true

module Limelight
  module Specs
    module SpecHelper

      def open_scene
        if @spec_helper_options[:stage]
          stage = producer.theater[@spec_helper_options[:stage]]
          raise "No such stage: '#{@spec_helper_options[:stage]}'" unless stage
        else
          stage = producer.theater.default_stage
        end
        @scene = producer.open_scene(@scene_name.to_s, stage)
      end

      def scene
        open_scene unless @scene
        return @scene
      end

    end
  end
end

module Spec
  module Example
    class ExampleGroup

      def self.uses_scene(scene_name, options = {})
        include Limelight::Specs::SpecHelper

        before(:each) do
          @scene_name = scene_name
          @spec_helper_options = options
          @scene = nil
        end
      end

      after(:suite) do
        $producer.theater.stages.each { |stage| stage.close } unless $producer.nil?
      end

      def producer
        if $producer.nil?
          if $with_ui
            Limelight::Main.initializeContext
          else
            Limelight::Main.initializeTestContext
          end
          raise "$PRODUCTION_PATH undefined.  Make sure you specify the location of the production in $PRODUCTION_PATH." unless defined?($PRODUCTION_PATH)
          raise "Could not find production: '#{$PRODUCTION_PATH}'. Check $PRODUCTION_PATH." unless File.exists?($PRODUCTION_PATH)
          $producer = Limelight::Producer.new($PRODUCTION_PATH)
          $producer.load
        end
        return $producer
      end

      def production
        return producer.production
      end

    end
  end
end

if !$with_ui
  module Limelight
    module UI
      module Model

        class Frame

          def open

          end

        end

      end
    end
  end
end