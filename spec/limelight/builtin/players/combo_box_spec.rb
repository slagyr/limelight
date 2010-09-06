#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../../spec_helper")
require 'limelight/scene'
require 'limelight/prop'
require 'limelight/builtin/players'

describe Limelight::Builtin::Players::ComboBox do

  before(:each) do
    @scene = Limelight::Scene.new(:casting_director => mock("caster", :fill_cast => nil))
    @prop = Limelight::Prop.new(:scene => @scene)
    Limelight::Player.cast(Limelight::Builtin::Players::ComboBox, @prop)
  end
  
  it "should have a ComboBox" do
    @prop.panel.children[0].class.should == Limelight::UI::Model::Inputs::ComboBoxPanel
  end
  
  it "should have settable value" do
    @prop.choices = ["1", "2", "3"]
    @prop.value.should == "1"
    
    @prop.value = "3"
    @prop.value.should == "3"
  end

end