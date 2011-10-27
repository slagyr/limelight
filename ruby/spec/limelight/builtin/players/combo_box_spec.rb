#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

require File.expand_path(File.dirname(__FILE__) + "/../../../spec_helper")
require 'limelight/scene'
require 'limelight/prop'
require 'limelight/builtin/players'

describe Limelight::Builtin::Players::ComboBox do

  before(:each) do
    @scene = Limelight::Scene.new(:casting_director => mock("caster"))
    @prop = Limelight::Prop.new(:scene => @scene)
    Limelight::Player.cast(Limelight::Builtin::Players::ComboBox, @prop)
  end

  it "should have a ComboBox" do
    @prop.peer.children[0].class.should == Java::limelight.ui.model.inputs.ComboBoxPanel
  end

  it "should have settable value" do
    @prop.choices = ["1", "2", "3"]
    @prop.value.should == "1"

    @prop.value = "3"
    @prop.value.should == "3"
  end

end