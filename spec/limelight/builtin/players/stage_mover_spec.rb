#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../../spec_helper")
require 'limelight/scene'
require 'limelight/prop'
require 'limelight/builtin/players'
require 'limelight/stage'

describe Limelight::Builtin::Players::StageMover do

  before(:each) do
    @stage = Limelight::Stage.new(nil, "stage name")
    @scene = Limelight::Scene.new
    @scene.stage = @stage
    @prop = Limelight::Prop.new()
    @scene.add(@prop)
    Limelight::Player.cast(Limelight::Builtin::Players::StageMover, @prop)
  end

  it "should respond to the right mouse events" do
    @prop.panel.event_handler.get_actions(Limelight::UI::Events::MousePressedEvent).size.should == 1
    @prop.panel.event_handler.get_actions(Limelight::UI::Events::MouseDraggedEvent).size.should == 1
  end

  it "should change the stage location when dragged" do
    @stage.frame.setLocation(0, 0)
    mouse.press(@prop, 100, 100)
    mouse.drag(@prop, 125, 150)

    @stage.frame.location.x.should == 25
    @stage.frame.location.y.should == 50
  end

end