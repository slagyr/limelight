#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../../spec_helper")
require 'limelight/prop'
require 'limelight/builtin/players/check_box'

describe Limelight::Builtin::Players::CheckBox do

  before(:each) do
    @prop = Limelight::Prop.new
    @prop.include_player(Limelight::Builtin::Players::CheckBox)
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