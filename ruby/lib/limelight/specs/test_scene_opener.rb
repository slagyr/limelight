#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Specs
    module SpecHelper

      class TestSceneOpener #:nodoc:
        def initialize(producer, ll_spec_options, prop_block)
          @producer = producer
          @prop_block = prop_block
          @production = producer.production
          @ll_spec_options = ll_spec_options
        end
  
        def create_player_helper
          player_name = @ll_spec_options[:with_player]
          Limelight.build_props(@scene, :build_loader => @production.root) do
            __test_prop({:name =>player_name, :players => "#{player_name}", :id => "#{player_name}"})
          end        
        end

        def setup_player_spec
          path = @production.scene_directory(@ll_spec_options[:scene_path]) 
          @scene = Scene.new(:production => @production, :casting_director => @producer.casting_director, :path => path, :name => @ll_spec_options[:scene_name])

          if @prop_block
            Limelight.build_props(@scene, :build_loader => @production.root, &@prop_block)
          elsif @ll_spec_options[:with_player]
            create_player_helper
          end

          @producer.load_styles(@scene.styles_file, @scene.styles_store)
          @stage.open(@scene)
        end

        def setup_stage
          if @ll_spec_options[:stage]
            @stage = @producer.theater[@ll_spec_options[:stage]]
            raise "No such stage: '#{@ll_spec_options[:stage]}'" unless @stage
          else
            @stage = @producer.theater.default_stage
          end

          @stage.should_remain_hidden = @ll_spec_options[:hidden] || true
        end

        def open_scene
          setup_stage

          if @ll_spec_options[:scene]
            @scene = @producer.open_scene(@ll_spec_options[:scene].to_s, @stage)
          elsif @ll_spec_options[:scene_path]
            setup_player_spec
          end
    
          return @scene
        end
      end
    end
  end
end