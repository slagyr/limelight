#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

require File.expand_path(File.dirname(__FILE__) + "/../../../spec_helper")
require 'limelight/scene'
require 'limelight/prop'
require 'limelight/builtin/players'
require 'limelight/stage'

describe Limelight::Builtin::Players::StageMover do

  before(:each) do
    @stage = Limelight::Stage.new(nil, "stage name")
    @scene = Limelight::Scene.new
    @scene.peer.stage = @stage.peer
    @prop = Limelight::Prop.new()
    @scene.add(@prop)
    Limelight::Player.cast(Limelight::Builtin::Players::StageMover, @prop)
  end

  unless_headless do

    it "should respond to the right mouse events" do
      @prop.peer.event_handler.get_actions(Java::limelight.ui.events.panel.MousePressedEvent).size.should == 1
      @prop.peer.event_handler.get_actions(Java::limelight.ui.events.panel.MouseDraggedEvent).size.should == 1
    end

    it "should change the stage location when dragged" do
      @stage.peer.frame.setLocation(0, 0)
      mouse.press(@prop, 100, 100)
      mouse.drag(@prop, 125, 150)

      @stage.peer.location.x.should == 25
      @stage.peer.location.y.should == 50
    end

  end

end