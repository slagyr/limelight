#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/data'

describe Limelight::Data do

  before(:each) do
    Limelight::Data.reset
    Limelight::Context.instance.os.stub!(:dataRoot).and_return("~/")
  end

  it "should have a Downloads dir" do
    expected = File.join(Limelight::Data.root, "Downloads")

    Limelight::Data.downloads_dir.should == expected
  end

  it "should have a Productions dir" do
    expected = File.join(Limelight::Data.root, "Productions")

    Limelight::Data.productions_dir.should == expected
  end
                                                                                      
  it "should establish all the dirs" do
    Limelight::Data.stub!(:root).and_return(TestDir.path("Limelight"))

    Limelight::Data.establish_data_dirs

    File.exists?(TestDir.path("Limelight")).should == true
    File.exists?(TestDir.path("Limelight/Downloads")).should == true
    File.exists?(TestDir.path("Limelight/Productions")).should == true
  end

end