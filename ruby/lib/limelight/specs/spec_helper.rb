#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

require 'rspec'
require File.expand_path(File.dirname(__FILE__) + "/../limelight_init")
require 'limelight/production'
require 'limelight/scene'
require 'limelight/string'
require 'limelight/mouse'

module Limelight

  # Limelight comes with builtin assistance for testing your productions with rspec.
  # See Limelight::Specs::SpecHelper
  #
  module Specs

    class << self
      attr_accessor :production
    end

    # Limelight comes with builtin assistance for testing your productions using RSpec. To get started, add the following
    # to your describe block...
    #
    #   uses_limelight :scene => "my_scene"
    #
    # This will add before(:each) blocks that will setup your production and load your scene before each test.
    # It also provides some accessors to common objects like the scene and production.  Afterwards, you can
    # write tests that look like this.
    #
    #   it "does something with the scene" do
    #     scene.name.should == "my_scene"
    #     scene.find("title").text.should == "This is the Title"
    #     production.theater["default_stage"].current_scene.should be(scene)
    #   end
    #
    # There are several other options you can supply with 'uses_limelight':
    #
    # :hidden - (true or false) Defaults to true, but if you turn it off, the scene will pop open on your screen. This
    # can be really nifty at times and really annoying at other time.
    #
    # :stage - The name of the stage you want the scene to be loaded on. The theater's default stage will be used by
    # default.
    #
    # :scene_name - To be used instead of the :scene option.  This will create an empty scene in your production with
    # the specified name.
    #
    # :scene_path - To be used in conjunction with :scene_name.  This will cause the empty scene to be loaded with
    # the specified path.  Styles and Players associated with the path will be applied to the created scene.
    #
    # The 'uses_limelight' methods also accepts block using the Limelight::DSL::PropBuilder DSL.  This is convenient for building a
    # simple prop structure sufficient to test the desired behavior.  Here's an example:
    #
    #   describe "my scene" do
    #     uses_limelight :scene_name => "some_name", :scene_path => "my_scene_path" do
    #       clicky :id => "clicky", :text => "click me", :on_mouse_clicked => "self.text = 'Hey! You clicked me!'"
    #     end
    #
    #     it "should change text on clicky when clicked" do
    #       scene.find("clicky").mouse_clicked(nil)
    #       clicky.text.should == "Hey!  You clicked me!"
    #     end
    #   end
    #
    module SpecHelper

      def production
        if Limelight::Specs.production.nil?
#          if $with_ui
          Java::limelight.Boot.boot
#          else
#            Limelight::Main.initializeTestContext
#          end
          raise "$PRODUCTION_PATH undefined.  Make sure you specify the location of the production in $PRODUCTION_PATH." unless defined?($PRODUCTION_PATH)
          raise "Could not find production: '#{$PRODUCTION_PATH}'. Check $PRODUCTION_PATH." unless File.exists?($PRODUCTION_PATH)
          Limelight::Specs.production = Limelight::Production.new(Java::limelight.ruby.RubyProduction.new($PRODUCTION_PATH))
          Limelight::Specs.production.peer.illuminateProduction
          Limelight::Specs.production.peer.loadProduction
        end
        Limelight::Specs.production
      end

      def mouse
        @mouse ||= Limelight::Mouse.new
        @mouse
      end

      def scene
        if @scene.nil?
          _setup_stage
          if @ll_scene_id
            @scene = production.open_scene(@ll_scene_id.to_s, :stage => @stage.name)
          elsif @ll_scene_path
            _setup_scene
          end
        end
        @scene
      end

      private #############################################

      def _init_ll_options(options)
        @ll_scene_id = options[:scene]
        @ll_scene_name = options[:scene_name]
        @ll_scene_path = options[:scene_path]
        @ll_stage_id = options[:stage]
        @ll_hidden = options[:hidden]
        @ll_player_names = options[:with_players]
        _configure_player_helpers()
      end

      def _configure_player_helpers
        if @ll_player_names
          @ll_player_names = @ll_player_names.is_a?(Array) ? @ll_player_names : [@ll_player_names]
          @ll_player_names = @ll_player_names.map { |n| n.to_s }
          @ll_player_names.each do |player_name|
            eval "def #{player_name}; scene.find('#{player_name}'); end;"
          end
        end
      end

      def _create_player_helpers
        player_names = @ll_player_names
        Limelight.build_props(@scene, :build_loader => production.root) do
          player_names.each do |player_name|
            __test_prop :name =>player_name, :players => "#{player_name}", :id => "#{player_name}"
          end
        end
      end

      def _setup_scene
        path = production.scene_directory(@ll_scene_path)
        @scene = Scene.new(:production => production, :path => path, :name => @ll_scene_name)
        _create_player_helpers
        Limelight.build_props(@scene, :build_loader => production.root, &@prop_block) if @prop_block

        production.load_styles(@scene.styles_file, @scene.styles)
        @stage.scene = @scene
        @stage.open
      end

      def _setup_stage
        if @ll_stage_id
          @stage = production.theater[@ll_stage_id]
          raise "No such stage: '#{@ll_stage_id}'" unless @stage
        else
          @stage = production.theater.default_stage
        end

        Java::limelight.ui.model.StageFrame.hiddenMode = @ll_hidden || true
      end
    end
  end
end

RSpec.configure do |config|
  config.after(:suite) do
    Java::limelight.Context.instance.killThreads
    #if Limelight::Specs.production
    #  Limelight::Specs.production.theater.stages.each do |stage|
    #    # MDM - We do this in a round-about way to reduce the chance of using stubbed or mocked methods.
    #    frame = stage.instance_variable_get("@frame")
    #    frame.close if frame
    #  end
    #end
    Java::java.awt.Window.getWindows.each do |window|
      window.dispose
    end
  end
end

module RSpec
  module Core
    class ExampleGroup

      def self.uses_limelight(options = {}, &prop_block)
        include Limelight::Specs::SpecHelper

        before(:all) do
          _init_ll_options(options)
          @prop_block = prop_block
        end

        before(:each) do
          @player = @scene = nil
        end
      end

    end
  end
end