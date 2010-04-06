#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../spec_helper")
require 'limelight/templates/templater'

describe Limelight::Templates::Templater do

  before(:each) do
    TestDir.clean
    @src_dir = File.join(TestDir.root, "src")
    @logger = mock("logger")
    @templater = Limelight::Templates::Templater.new(TestDir.root, @src_dir)
    @templater.logger = @logger
  end

  it "should have a templater logger" do
    @templater = Limelight::Templates::Templater.new(TestDir.root, @src_dir)
    @templater.logger.should_not == nil
    @templater.logger.class.should == Limelight::Templates::TemplaterLogger
  end

  it "should have a target root and source root directory" do
    @templater.target_root.should == TestDir.root
    @templater.source_root.should == @src_dir
  end

  it "should prefix target root with dot if not absolute" do
    Limelight::Templates::Templater.clarify("blah").should == "./blah"
    Limelight::Templates::Templater.clarify("one/two").should == "./one/two"
    Limelight::Templates::Templater.clarify("/root").should == "/root"
    Limelight::Templates::Templater.clarify("./blah").should == "./blah"
    Limelight::Templates::Templater.clarify("../blah").should == "../blah"
  end

  it "should create a directory" do
    @logger.should_receive(:creating_directory).with("#{TestDir.root}/blah")
    
    @templater.directory "blah"

    File.exists?(File.join(TestDir.root, "blah")).should == true
  end

  it "should create nested directories" do
    @logger.should_receive(:creating_directory).with("#{TestDir.root}/dir1")
    @logger.should_receive(:creating_directory).with("#{TestDir.root}/dir1/dir2")

    @templater.directory "dir1/dir2"

    File.exists?(File.join(TestDir.root, "dir1/dir2")).should == true
  end

  it "should create files from source" do
    TestDir.create_file("src/file1.txt", "blah")
    @logger.should_receive(:creating_file).with("#{TestDir.root}/f1.txt")

    @templater.file "f1.txt", "file1.txt"

    File.exists?(File.join(TestDir.root, "f1.txt")).should == true
    IO.read(File.join(TestDir.root, "f1.txt")).should == "blah"
  end

  it "should create files nested in directories" do
    TestDir.create_file("src/file1.txt", "blah")
    
    @logger.should_receive(:creating_directory).with("#{TestDir.root}/dir1")
    @logger.should_receive(:creating_file).with("#{TestDir.root}/dir1/f1.txt")

    @templater.file "dir1/f1.txt", "file1.txt"

    path = File.join(TestDir.root, "dir1", "f1.txt")
    File.exists?(path).should == true
    IO.read(path).should == "blah"
  end

  it "should skip files that already exist" do
    TestDir.create_file("src/file1.txt", "blah")
    TestDir.create_file("f1.txt", "existing content") 

    @logger.should_receive(:file_already_exists).with("#{TestDir.root}/f1.txt")

    @templater.file "f1.txt", "file1.txt"

    IO.read(File.join(TestDir.root, "f1.txt")).should == "existing content"
  end

  it "should replace one token in files" do
    TestDir.create_file("src/file1.txt", "abc !-TOKEN1-! 123")
    @logger.should_receive(:creating_file).with("#{TestDir.root}/f1.txt")

    @templater.file "f1.txt", "file1.txt", :TOKEN1 => "you know me"

    File.exists?(File.join(TestDir.root, "f1.txt")).should == true
    IO.read(File.join(TestDir.root, "f1.txt")).should == "abc you know me 123"
  end

  it "should replace multiple tokens in files" do
    TestDir.create_file("src/file1.txt", "!-TOKEN0-! !-TOKEN1-! !-TOKEN2-!")
    @logger.should_receive(:creating_file).with("#{TestDir.root}/f1.txt")

    @templater.file "f1.txt", "file1.txt", :TOKEN0 => "1", :TOKEN1 => "2", :TOKEN2 => "3"

    File.exists?(File.join(TestDir.root, "f1.txt")).should == true
    IO.read(File.join(TestDir.root, "f1.txt")).should == "1 2 3"
  end

end