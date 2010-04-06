#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../spec_helper")
require 'limelight/templates/templater_logger'

describe Limelight::Templates::TemplaterLogger do

  before(:each) do
    @logger = Limelight::Templates::TemplaterLogger.new
    @in, @out = IO.pipe
    @logger.output = @out
  end

  it "should have standard out as output" do
    @logger = Limelight::Templates::TemplaterLogger.new
    @logger.output.should == STDOUT
  end

  it "should handle creating directories" do
    @logger.creating_directory("dir1")

    result = @in.readline
    result.strip.should == "creating directory:  dir1"
  end

  it "should handle creating files" do
    @logger.creating_file("file1")

    result = @in.readline
    result.strip.should == "creating file:       file1"
  end

  it "should handle file already exists" do
    @logger.file_already_exists("file1")

    result = @in.readline
    result.strip.should == "file already exists: file1"
  end

end
