#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../../spec_helper")
require 'limelight/prop'
require 'limelight/builtin/players/password_box'

describe Limelight::Builtin::Players::PasswordBox do

  before(:each) do
    @prop = Limelight::Prop.new
    @prop.include_player(Limelight::Builtin::Players::PasswordBox)
  end

  it "should have a PasswordBox" do
    @prop.panel.children[0].class.should == Limelight::UI::Model::Inputs::PasswordBoxPanel
  end
  
  it "should use the PasswordBox for the text accessor" do
     @prop.text = "blah"
     @prop.panel.children[0].text.should == "blah"
     
     @prop.panel.children[0].text = "harumph"
     @prop.text.should == "harumph"
   end

end