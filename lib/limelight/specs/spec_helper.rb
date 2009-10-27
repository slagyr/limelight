#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'spec'
require File.expand_path(File.dirname(__FILE__) + "/../../init")
require 'limelight/scene'
require 'limelight/producer'
require 'limelight/string'
require 'limelight/specs/test_scene_opener'

module Limelight
  module Specs

    class << self
      attr_accessor :producer
    end
    
    module SpecHelper
      def scene
        if !@scene
          @scene = TestSceneOpener.new(producer, @ll_spec_options, @prop_block).open_scene 
        end
        return @scene
      end
    end
  end
end

module Spec
  module Example
    class ExampleGroup

      def self.uses_scene(scene_name, options = {})
        uses_limelight({:scene => scene_name}.merge(options))
      end
      
      def self.uses_limelight(options = {}, &prop_block)
        include Limelight::Specs::SpecHelper
        
        before(:each) do
          @ll_spec_options = options
          @prop_block = prop_block
          @player = @scene = nil
          create_accessor_for(@ll_spec_options[:with_player]) if @ll_spec_options[:with_player]
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
      
      def create_accessor_for(player_name)
        accessor = <<-EOF
          def #{player_name}
            return scene.find('#{player_name}')
          end
EOF
        eval(accessor)
      end
    end
  end
end