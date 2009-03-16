#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/commands/freeze_command'

describe Limelight::Commands::FreezeCommand do

  before(:all) do
    @command_class = Limelight::Commands::FreezeCommand
    @command = @command_class.new
  end

  before(:each) do
    @command = @command_class.new
    @command.instance_eval("def usage(e=nil); raise 'Usage called! ' + e; end;")
  end

  it "should be listed" do
    Limelight::Commands::LISTING["freeze"].should == @command_class
  end

  it "should parse no options" do
    @command.parse ["some_gem"]

    @command.gem_name.should == "some_gem"
    @command.production_path.should == "."
    @command.gem_version.should == nil
  end

  it "should parse production path option" do
    @command.parse ["-p", "prod/path", "some_gem"]
    @command.gem_name.should == "some_gem"
    @command.production_path.should == "prod/path"

    @command.parse ["--production=prod/path2", "some_gem"]
    @command.gem_name.should == "some_gem"
    @command.production_path.should == "prod/path2"
  end

  it "should parse version option" do
    @command.parse ["-v", "1.2.3", "some_gem"]
    @command.gem_name.should == "some_gem"
    @command.gem_version.should == "1.2.3"

    @command.parse ["--version=3.2.1", "some_gem"]
    @command.gem_name.should == "some_gem"
    @command.gem_version.should == "3.2.1"
  end

  it "should check if it's a gem file" do
    @command.is_gem_file?("some.gem").should == true
    @command.is_gem_file?("some-java_0.2.1.gem").should == true
    @command.is_gem_file?("/some.gem").should == true
    @command.is_gem_file?("/dir/dir2/some.gem").should == true
    @command.is_gem_file?("../dir/dir2/some.gem").should == true

    @command.is_gem_file?("some_gem").should == false
    @command.is_gem_file?("some_gem-java.0.3.1").should == false
  end

end