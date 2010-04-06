#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/file_loader'

describe Limelight::FileLoader do

  before(:each) do
    @loader = Limelight::FileLoader.for_root("/Users/micahmartin/Projects/limelight/example")
  end
  
  it "should load path parts" do
    @loader.root.should == "/Users/micahmartin/Projects/limelight/example"
  end
  
  it "should calculate relative file paths" do
    @loader.path_to(".").should == "/Users/micahmartin/Projects/limelight/example"
    @loader.path_to("..").should == "/Users/micahmartin/Projects/limelight"
    @loader.path_to("images/blah.gif").should == "/Users/micahmartin/Projects/limelight/example/images/blah.gif"
  end
  
  it "should know absolute paths" do
    @loader.path_to("/").should == "/"
    @loader.path_to("/Users").should == "/Users"
  end

end
