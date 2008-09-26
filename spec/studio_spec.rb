require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/studio'

describe Limelight::Studio do

  it "should install itsself" do
    Limelight::Studio.install

    studio = Limelight::Studio.instance
    Limelight::Context.instance.studio.should == studio 
  end


end