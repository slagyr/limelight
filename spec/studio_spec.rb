#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/studio'

describe Limelight::Studio do

  it "should install itsself" do
    Limelight::Studio.install

    studio = Limelight::Studio.instance
    Limelight::Context.instance.studio.should == studio 
  end


end