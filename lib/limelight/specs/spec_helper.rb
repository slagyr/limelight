#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'spec'
require File.expand_path(File.dirname(__FILE__) + "/../../init")
require 'limelight/scene'
require 'limelight/producer'

$producer = nil
$with_ui = true

module Spec
  module Example
    class ExampleGroup

      def self.uses_scene(scene_name)
        before(:each) do
          @scene = producer.open_scene(scene_name.to_s, producer.theater["default"])
        end

        attr_reader :scene
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
          $producer = Limelight::Producer.new($PRODUCTION_PATH)
          $producer.load
        end
        return $producer
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