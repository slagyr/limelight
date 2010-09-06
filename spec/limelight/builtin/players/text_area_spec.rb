#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../../spec_helper")
require 'limelight/prop'
require 'limelight/builtin/players'

describe Limelight::Builtin::Players::TextArea do

  before(:each) do
    @prop = Limelight::Prop.new
    Limelight::Player.cast(Limelight::Builtin::Players::TextArea, @prop)
  end
  
  it "should have a JTextArea" do
    @prop.panel.children[0].class.should == Limelight::UI::Model::Inputs::TextAreaPanel
  end
  
  it "should use the TextArea for the text accessor" do
    @prop.text = "blah"
    @prop.panel.children[0].text.should == "blah"
    
    @prop.panel.text = "harumph"
    @prop.text.should == "harumph"
  end

end