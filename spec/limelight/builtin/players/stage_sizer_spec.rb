#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../../spec_helper")
require 'limelight/scene'
require 'limelight/prop'
require 'limelight/builtin/players/stage_sizer'
require 'limelight/stage'

describe Limelight::Builtin::Players::StageSizer do

  before(:each) do
    @stage = Limelight::Stage.new(nil, "stage name")
    @scene = Limelight::Scene.new
    @scene.stage = @stage
    @prop = Limelight::Prop.new()
    @scene.add(@prop)
    @prop.include_player(Limelight::Builtin::Players::StageSizer)
  end

  it "should respond to the right mouse events" do
    methods = Limelight::Builtin::Players::StageSizer.instance_methods
    methods.include?("mouse_pressed").should == true
    methods.include?("mouse_dragged").should == true
  end

  it "should change the stage dimension when dragged" do
    @stage.frame.setSize(500, 500)
    @prop.mouse_pressed(MouseEvent.new(100, 100))
    @prop.mouse_dragged(MouseEvent.new(125, 150))

    @stage.frame.size.width.should == 525
    @stage.frame.size.height.should == 550

    @prop.mouse_dragged(MouseEvent.new(150, 175))
    @stage.frame.size.width.should == 550
    @stage.frame.size.height.should == 575
  end

end