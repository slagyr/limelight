#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

require File.expand_path(File.dirname(__FILE__) + "/../../../spec_helper")
require 'limelight/prop'
require 'limelight/builtin/players'

describe Limelight::Builtin::Players::TextArea do

  before(:each) do
    @prop = Limelight::Prop.new
    Limelight::Player.cast(Limelight::Builtin::Players::TextArea, @prop)
  end
  
  it "should have a JTextArea" do
    @prop.peer.children[0].class.should == Java::limelight.ui.model.inputs.TextAreaPanel
  end
  
  it "should use the TextArea for the text accessor" do
    @prop.text = "blah"
    @prop.peer.children[0].text.should == "blah"
    
    @prop.peer.text = "harumph"
    @prop.text.should == "harumph"
  end

end