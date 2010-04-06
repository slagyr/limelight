#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.dirname(__FILE__) + '/../spec_helper'

describe Limelight::PackageServer::Package do

  before(:each) do
    @path = "/path/to/productions"
    Kernel.stub!(:system)
    File.stub!(:delete)
  end
  
  it "should find by name" do
    Kernel.should_receive(:system).with("jruby -S limelight pack #{@path}/langstons_ant")
    IO.should_receive(:read).with("langstons_ant.llp").and_return("the contents")
    File.stub!(:exists?).with("/path/to/productions/langstons_ant").and_return(true)
    File.stub!(:exists?).with("langstons_ant.llp").and_return(true)    

    package = Limelight::PackageServer::Package.find_by_name("langstons_ant", @path)

    package.file_name.should == "langstons_ant.llp"
    package.contents.should == "the contents"
  end

  it "should return empty package contents if llp isn't created" do
    IO.should_not_receive(:read).with("langstons_ant.llp").and_return("the contents")
    File.stub!(:exists?).with("langstons_ant.llp").and_return(false)
    File.stub!(:exists?).with("/path/to/productions/langstons_ant").and_return(true)

    package = Limelight::PackageServer::Package.find_by_name("langstons_ant", @path)

    package.file_name.should == nil
    package.contents.should == nil
  end
  
  it "should delete existing llp file if exists" do
    File.stub!(:exists?).and_return(true)
    Kernel.stub!(:system)
    IO.stub!(:read)
    File.should_receive(:delete).with("langstons_ant.llp")
    
    package = Limelight::PackageServer::Package.find_by_name("langstons_ant", @path)
  end
  
  it "should not try to package a directory that doesn't exist" do
    Kernel.should_not_receive(:system).with("jruby -S limelight pack #{@path}/langstons_ant")
    File.should_receive(:exists?).with("/path/to/productions/langstons_ant").and_return(false)
    
    package = Limelight::PackageServer::Package.find_by_name("langstons_ant", @path)
    
    package.file_name.should == nil
    package.contents.should == nil
  end
  

end