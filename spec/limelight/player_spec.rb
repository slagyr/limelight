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

  [{:name => "on_mouse_clicked", :klass => Limelight::UI::Events::MouseClickedEvent},
   {:name => "on_mouse_pressed", :klass => Limelight::UI::Events::MousePressedEvent},
   {:name => "on_mouse_released", :klass => Limelight::UI::Events::MouseReleasedEvent},
   {:name => "on_mouse_moved", :klass => Limelight::UI::Events::MouseMovedEvent},
   {:name => "on_mouse_dragged", :klass => Limelight::UI::Events::MouseDraggedEvent},
   {:name => "on_mouse_entered", :klass => Limelight::UI::Events::MouseEnteredEvent},
   {:name => "on_mouse_exited", :klass => Limelight::UI::Events::MouseExitedEvent}
  ].each do |event|
    it "handles #{event[:name]} actions" do
      @player.module_eval "#{event[:name]} { $RECIPIENT = self }"
      Limelight::Player.cast(@player, @prop)
      @prop.panel.event_handler.dispatch(event[:klass].new(@prop.panel, 0, nil, 0))
      $RECIPIENT.should == @prop
    end
  end

  it "handles on_mouse_wheel actions" do
    @player.module_eval "on_mouse_wheel { $RECIPIENT = self }"
    Limelight::Player.cast(@player, @prop)
    @prop.panel.event_handler.dispatch(Limelight::UI::Events::MouseWheelEvent.new(@prop.panel, 0, nil, 1, 0, 0, 0))
    $RECIPIENT.should == @prop
  end

  [{:name => "on_key_pressed", :klass => Limelight::UI::Events::KeyPressedEvent},
   {:name => "on_key_released", :klass => Limelight::UI::Events::KeyReleasedEvent}
  ].each do |event|
    it "handles #{event[:name]} actions" do
      @player.module_eval "#{event[:name]} { $RECIPIENT = self }"
      Limelight::Player.cast(@player, @prop)
      @prop.panel.event_handler.dispatch(event[:klass].new(@prop.panel, 0, 0, 0))
      $RECIPIENT.should == @prop
    end
  end

  it "handles on_char_typed actions" do
    @player.module_eval "on_char_typed { $RECIPIENT = self }"
    Limelight::Player.cast(@player, @prop)
    @prop.panel.event_handler.dispatch(Limelight::UI::Events::CharTypedEvent.new(@prop.panel, 0, 0))
    $RECIPIENT.should == @prop
  end

  [ {:name => "on_focus_gained", :klass => Limelight::UI::Events::FocusGainedEvent},
    {:name => "on_focus_lost", :klass => Limelight::UI::Events::FocusLostEvent},
    {:name => "on_button_pushed", :klass => Limelight::UI::Events::ButtonPushedEvent},
    {:name => "on_value_changed", :klass => Limelight::UI::Events::ValueChangedEvent}
  ].each do |event|
    it "handles #{event[:name]} actions" do
      @player.module_eval "#{event[:name]} { $RECIPIENT = self }"
      Limelight::Player.cast(@player, @prop)
      @prop.panel.event_handler.dispatch(event[:klass].new(@prop.panel))
      $RECIPIENT.should == @prop
    end
  end
  
  it "handles on_scene_opened actions" do
    scene = Limelight::Scene.new
    @player.module_eval "on_scene_opened { $RECIPIENT = self }"
    Limelight::Player.cast(@player, scene)
    scene.panel.event_handler.dispatch(Limelight::UI::Events::SceneOpenedEvent.new(scene.panel))
    $RECIPIENT.should == scene
  end

end