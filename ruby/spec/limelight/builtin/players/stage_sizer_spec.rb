##- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
##- Limelight and all included source files are distributed under terms of the MIT License.
#
#require File.expand_path(File.dirname(__FILE__) + "/../../../spec_helper")
#require 'limelight/scene'
#require 'limelight/prop'
#require 'limelight/builtin/players'
#require 'limelight/stage'
#
#describe Limelight::Builtin::Players::StageSizer do
#
#  before(:each) do
#    @stage = Limelight::Stage.new(nil, "stage name")
#    @scene = Limelight::Scene.new
#    @scene.peer.stage = @stage.peer
#    @prop = Limelight::Prop.new()
#    @scene.add(@prop)
#    Limelight::Player.cast(Limelight::Builtin::Players::StageSizer, @prop)
#  end
#
#  unless_headless do
#
#  it "responds to the right mouse events" do
#    @prop.peer.event_handler.get_actions(Java::limelight.ui.events.panel.MousePressedEvent).size.should == 1
#    @prop.peer.event_handler.get_actions(Java::limelight.ui.events.panel.MouseDraggedEvent).size.should == 1
#  end
#
#    it "changes the stage dimension when dragged" do
#      @stage.peer.frame.setSize(500, 500)
#      mouse.press(@prop, 100, 100)
#      mouse.drag(@prop, 125, 150)
#
#      @stage.peer.size.width.should == 525
#      @stage.peer.size.height.should == 550
#
#      mouse.drag(@prop, 150, 175)
#      @stage.peer.size.width.should == 550
#      @stage.peer.size.height.should == 575
#    end
#
#  end
#
#end