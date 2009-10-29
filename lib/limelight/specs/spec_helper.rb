#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'spec'
require File.expand_path(File.dirname(__FILE__) + "/../../init")
require 'limelight/scene'
require 'limelight/producer'
require 'limelight/string'
require 'limelight/specs/test_scene_opener'

module Limelight

  # Limelight comes with builtin assistance for testing your productions with rspec.
  # See Limelight::Specs::SpecHelper
  #
  module Specs

    class << self
      attr_accessor :producer
    end

    # Limelight comes with builtin assistance for testing your productions with rspec. To get started, add the following
    # to your describe block...
    #
    #   uses_limelight :scene => "my_scene"
    #
    # This will add before(:each) blocks that will setup your production and load your scene before each test.
    # It also provides some accessors to common objects like the scene and production.  Afterwards, you can
    # write tests that look like this.
    #
    #   it "should do something with the scene" do
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
      def scene
        if !@scene
          @scene = TestSceneOpener.new(producer, @ll_spec_options, @prop_block).open_scene 
        end
        return @scene
      end
    end
  end
end

module Spec #:nodoc:
  module Example
    class ExampleGroup

      # Deprecated
      #
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