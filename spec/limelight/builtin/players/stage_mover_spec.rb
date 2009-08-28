#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../../spec_helper")
require 'limelight/scene'
require 'limelight/prop'
require 'limelight/builtin/players/stage_mover'
require 'limelight/stage'

describe Limelight::Builtin::Players::StageMover do

  before(:each) do
    @stage = Limelight::Stage.new(nil, "stage name")
    @scene = Limelight::Scene.new
    @scene.stage = @stage
    @prop = Limelight::Prop.new()
    @scene.add(@prop)
    @prop.include_player(Limelight::Builtin::Players::StageMover)
  end

  it "should respond to the right mouse events" do
    methods = Limelight::Builtin::Players::StageMover.instance_methods
    methods.include?("mouse_pressed").should == true
    methods.include?("mouse_dragged").should == true
  end

  it "should change the stage location when dragged" do
    @stage.frame.setLocation(0, 0)
    @prop.mouse_pressed(MouseEvent.new(100, 100))
    @prop.mouse_dragged(MouseEvent.new(125, 150))

    @stage.frame.location.x.should == 25
    @stage.frame.location.y.should == 50
  end

end