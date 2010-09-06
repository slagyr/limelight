require File.expand_path(File.dirname(__FILE__) + "/../../spec_helper")
require 'limelight/builtin/players'


describe Limelight::Builtin::Players do

  it "has button" do
    Limelight::Builtin::Players::Button.is_a?(Limelight::Player).should == true
  end

end