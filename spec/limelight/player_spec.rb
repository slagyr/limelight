require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')
require 'limelight/player'
require 'limelight/prop'
require 'limelight/scene'

describe Limelight::Player do

  before do
    @prop = Limelight::Prop.new
    @player = Limelight::Player.new
    $RECIPIENT = nil
  end

  it "will call on_cast when extending a prop" do
    @player.module_eval "on_cast { $CAST_PROP = self }"

    Limelight::Player.cast(@player, @prop)

    $CAST_PROP.should == @prop
    @prop.is_a?(@player).should == true
  end

  it "only casts a prop once per player" do
    $CAST_COUNT = 0
    @player.module_eval "on_cast { $CAST_COUNT += 1 }"

    Limelight::Player.cast(@player, @prop)
    Limelight::Player.cast(@player, @prop)

    $CAST_COUNT.should == 1
  end

  it "can cast multiple props" do
    $CASTS = []
    @player.module_eval "on_cast { $CASTS << self }"
    prop2 = Limelight::Prop.new

    Limelight::Player.cast(@player, @prop)
    Limelight::Player.cast(@player, prop2)

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
      Limelight::Player.cast(@player, @prop)
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
    Limelight::Player.cast(@player, @prop)
    Java::limelight.ui.events.panel.MouseWheelEvent.new(0, nil, 1, 0, 0, 0).dispatch(@prop.peer)
    $RECIPIENT.should == @prop
  end

  [{:name => "on_key_pressed", :klass => Java::limelight.ui.events.panel.KeyPressedEvent},
   {:name => "on_key_released", :klass => Java::limelight.ui.events.panel.KeyReleasedEvent}
  ].each do |event|
    it "handles #{event[:name]} actions" do
      @player.module_eval "#{event[:name]} { $RECIPIENT = self }"
      Limelight::Player.cast(@player, @prop)
      event[:klass].new(0, 0, 0).dispatch(@prop.peer)
      $RECIPIENT.should == @prop
    end
  end

  it "handles on_char_typed actions" do
    @player.module_eval "on_char_typed { $RECIPIENT = self }"
    Limelight::Player.cast(@player, @prop)
    Java::limelight.ui.events.panel.CharTypedEvent.new(0, 0).dispatch(@prop.peer)
    $RECIPIENT.should == @prop
  end

  [ {:name => "on_focus_gained", :klass => Java::limelight.ui.events.panel.FocusGainedEvent},
    {:name => "on_focus_lost", :klass => Java::limelight.ui.events.panel.FocusLostEvent},
    {:name => "on_button_pushed", :klass => Java::limelight.ui.events.panel.ButtonPushedEvent},
    {:name => "on_value_changed", :klass => Java::limelight.ui.events.panel.ValueChangedEvent}
  ].each do |event|
    it "handles #{event[:name]} actions" do
      @player.module_eval "#{event[:name]} { $RECIPIENT = self }"
      Limelight::Player.cast(@player, @prop)
      event[:klass].new.dispatch(@prop.peer)
      $RECIPIENT.should == @prop
    end
  end
  
  it "handles on_scene_opened actions" do
    scene = Limelight::Scene.new
    @player.module_eval "on_scene_opened { $RECIPIENT = self }"
    Limelight::Player.cast(@player, scene)
    Java::limelight.ui.events.panel.SceneOpenedEvent.new.dispatch(scene.peer)
    $RECIPIENT.should == scene
  end

end