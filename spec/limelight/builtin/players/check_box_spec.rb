#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../../spec_helper")
require 'limelight/prop'
require 'limelight/builtin/players'

describe Limelight::Builtin::Players::CheckBox do

  before(:each) do
    @prop = Limelight::Prop.new
    Limelight::Player.cast(Limelight::Builtin::Players::CheckBox, @prop)
  end
  
  it "should have a TextField" do
    @prop.panel.children[0].class.should == Limelight::UI::Model::Inputs::CheckBoxPanel
  end
  
  it "should handled checked state" do
    @prop.checked?.should == false
    @prop.checked.should == false
    
    @prop.checked = true
    
    @prop.checked?.should == true
    @prop.checked.should == true
  end

end