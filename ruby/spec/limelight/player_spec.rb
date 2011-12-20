#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')
require 'limelight/player'
require 'limelight/prop'
require 'limelight/scene'

describe Limelight::Player do

  before do
    @prop = Limelight::Prop.new
    @player = Limelight::Player.new("bill", "some/path.rb")
    $RECIPIENT = nil
  end

  it "has a name and path" do
    @player.name.should == "bill"
    @player.path.should == "some/path.rb"
  end

  it "will call on_cast when extending a prop" do
    @player.module_eval "on_cast { $CAST_PROP = self }"

    @player.cast(@prop.peer)

    $CAST_PROP.should == @prop
    @prop.is_a?(@player).should == true
  end

  it "only casts a prop once per player" do
    $CAST_COUNT = 0
    @player.module_eval "on_cast { $CAST_COUNT += 1 }"

    @player.cast(@prop.peer)
    @player.cast(@prop.peer)

    $CAST_COUNT.should == 1
  end

  it "can cast multiple props" do
    $CASTS = []
    @player.module_eval "on_cast { $CASTS << self }"
    prop2 = Limelight::Prop.new

    @player.cast(@prop)
    @player.cast(prop2)

    $CASTS.should == [@prop, prop2]
  end

  [{:name => "on_mouse_clicked", :klass => Java::limelight.ui.events.panel.MouseClickedEvent},
   {:name => "on_mouse_pressed", :klass => Java::limelight.ui.events.panel.MousePressedEvent},
   {:name => "on_mouse_released", :klass => Java::limelight.ui.events.panel.MouseReleasedEvent},
   {:name => "on_mouse_moved", :klass => Java::limelight.ui.events.panel.MouseMovedEvent},
   {:name => "on_mouse_dragged", :klass => Java::limelight.ui.events.panel.MouseDraggedEvent},
   {:name => "on_mouse_entered", :klass => Java::limelight.ui.events.panel.MouseEnteredEvent},
   {:name => "on_mouse_exited", :klass => Java::limelight.ui.events.panel.MouseExitedEvent}
  ].each do |event|
    it "handles #{event[:name]} actions" do
      @player.module_eval "#{event[:name]} { $RECIPIENT = self }"
      @player.cast(@prop.peer)
      begin
        event[:klass].new(0, nil, 0).dispatch(@prop.peer)
      rescue StandardError => e
        puts "e: #{e} #{event.inspect}"
        raise e
      end
      $RECIPIENT.should == @prop
    end
  end

  it "handles on_mouse_wheel actions" do
    @player.module_eval "on_mouse_wheel { $RECIPIENT = self }"
    @player.cast(@prop.peer)
    Java::limelight.ui.events.panel.MouseWheelEvent.new(0, nil, 1, 0, 0, 0).dispatch(@prop.peer)
    $RECIPIENT.should == @prop
  end

  [{:name => "on_key_pressed", :klass => Java::limelight.ui.events.panel.KeyPressedEvent},
   {:name => "on_key_released", :klass => Java::limelight.ui.events.panel.KeyReleasedEvent}
  ].each do |event|
    it "handles #{event[:name]} actions" do
      @player.module_eval "#{event[:name]} { $RECIPIENT = self }"
      @player.cast(@prop.peer)
      event[:klass].new(0, 0, 0).dispatch(@prop.peer)
      $RECIPIENT.should == @prop
    end
  end

  it "handles on_char_typed actions" do
    @player.module_eval "on_char_typed { $RECIPIENT = self }"
    @player.cast(@prop.peer)
    Java::limelight.ui.events.panel.CharTypedEvent.new(0, 0).dispatch(@prop.peer)
    $RECIPIENT.should == @prop
  end

  [{:name => "on_focus_gained", :klass => Java::limelight.ui.events.panel.FocusGainedEvent},
   {:name => "on_focus_lost", :klass => Java::limelight.ui.events.panel.FocusLostEvent},
   {:name => "on_button_pushed", :klass => Java::limelight.ui.events.panel.ButtonPushedEvent},
   {:name => "on_value_changed", :klass => Java::limelight.ui.events.panel.ValueChangedEvent}
  ].each do |event|
    it "handles #{event[:name]} actions" do
      @player.module_eval "#{event[:name]} { $RECIPIENT = self }"
      @player.cast(@prop.peer)
      event[:klass].new.dispatch(@prop.peer)
      $RECIPIENT.should == @prop
    end
  end

  it "handles on_scene_opened actions" do
    scene = Limelight::Scene.new
    @player.module_eval "on_scene_opened { $RECIPIENT = self }"
    @player.cast(scene)
    Java::limelight.ui.events.panel.SceneOpenedEvent.new.dispatch(scene.peer)
    $RECIPIENT.should == scene
  end

  it "handles on_illuminated actions" do
    @player.module_eval "on_illuminated { $RECIPIENT = self }"
    @player.cast(@prop.peer)
    Java::limelight.ui.events.panel.IlluminatedEvent.new(@prop.peer).dispatch(@prop.peer)
    $RECIPIENT.should == @prop
  end

  it "handles the prop_reader helper" do
    scene = Limelight::Scene.new
    waldo = Limelight::Prop.new(:id => "waldo")
    prop = Limelight::Prop.new
    scene << waldo << prop
    scene.peer.illuminate

    @player.module_eval "prop_reader :waldo"
    @player.cast(prop)

    prop.waldo.should == waldo
  end

  it "handles stagehand_reader helper" do
    prop = Limelight::Prop.new
    prop.stagehands["thing-one"] = 1
    @player.module_eval "stagehand_reader :thing_one"
    @player.cast(prop)

    prop.thing_one.should == 1
  end

end