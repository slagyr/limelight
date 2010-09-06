#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../../spec_helper")
require 'limelight/scene'
require 'limelight/prop'
require 'limelight/builtin/players'
require 'limelight/stage'

describe Limelight::Builtin::Players::StageSizer do

  before(:each) do
    @stage = Limelight::Stage.new(nil, "stage name")
    @scene = Limelight::Scene.new
    @scene.stage = @stage
    @prop = Limelight::Prop.new()
    @scene.add(@prop)
    Limelight::Player.cast(Limelight::Builtin::Players::StageSizer, @prop)
  end

  it "should respond to the right mouse events" do
    @prop.panel.event_handler.get_actions(Limelight::UI::Events::MousePressedEvent).size.should == 1
    @prop.panel.event_handler.get_actions(Limelight::UI::Events::MouseDraggedEvent).size.should == 1
  end

  it "should change the stage dimension when dragged" do
    @stage.frame.setSize(500, 500)
    mouse.press(@prop, 100, 100)
    mouse.drag(@prop, 125, 150)

    @stage.frame.size.width.should == 525
    @stage.frame.size.height.should == 550
                            
    mouse.drag(@prop, 150, 175)
    @stage.frame.size.width.should == 550
    @stage.frame.size.height.should == 575
  end

end