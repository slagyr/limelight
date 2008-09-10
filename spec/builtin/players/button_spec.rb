#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../spec_helper")
require 'limelight/scene'
require 'limelight/prop'
require 'limelight/builtin/players/button'

describe Limelight::Builtin::Players::Button do

  before(:each) do
    @scene = Limelight::Scene.new(:casting_director => make_mock("caster", :fill_cast => nil))
    @prop = Limelight::Prop.new
    @prop.include_player(Limelight::Builtin::Players::Button)
  end
  
  it "should have a Button" do
    @prop.panel.children[0].class.should == Limelight::UI::Model::Inputs::ButtonPanel
  end

end