#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../spec_helper")
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
  
  describe "Actual Freezing" do
    
    before(:each) do
      stub_limelight_production    
      stub_gem_cache
      stub_unpacking_gem
      stub_establishing_gem_dir_in('prod/path', 'MicahGem-1.0')
      stub_templater
      stub_gem_installer
      FileUtils.stub!(:copy)
    end
    
    it "should create the specifications directory" do
      @templater.should_receive(:directory).with(File.join("__resources", "gems", "specifications"))

      @command.run ["-p", "prod/path", "some_gem"]
    end
    
    it "should copy the spec file from the Gem path" do
      path_to_specifications = "PATH"
      Gem.stub!(:dir).and_return(path_to_specifications)
      FileUtils.should_receive(:copy).with(File.join(path_to_specifications, "MicahGem-1.0.gemspec"),
                                           File.join("prod/path", "__resources", "gems", "specifications"))
                                          
                                          
      @command.run ["-p", "prod/path", "some_gem"]
    end
    
    def stub_limelight_production
      require 'limelight/util'

      Limelight::Util.stub!(:is_limelight_production?).and_return(true)
      Limelight::Util.stub!(:is_limelight_scene?).and_return(true)
    end

    def stub_unpacking_gem
      require 'rubygems/commands/unpack_command'

      unpack_command = mock(Gem::Commands::UnpackCommand, :get_path => "MicahGem-1.0.gem")
      Gem::Commands::UnpackCommand.stub!(:new).and_return(unpack_command)
      File.stub!(:exists?).with("MicahGem-1.0.gem").and_return(true)
    end

    def stub_gem_cache
      mock_gem = mock(Gem, :version => mock("Version", :version => "1.0", :to_s => "1.0"), :name => "MicahGem", :require_paths => nil)
      Gem.stub!(:cache).and_return(mock("Cache", :find_name => [mock_gem]))
    end

    def stub_establishing_gem_dir_in(project_dir, gem_dir)
      File.stub!(:exists?).with(File.join(project_dir, "__resources", "gems", "gems", gem_dir)).and_return(false)
    end

    def stub_templater
      require 'limelight/templates/templater'

      logger = mock("Logger", :log => nil)
      @templater = mock("Templater", :file => nil, :directory => nil, :logger => logger)
      Limelight::Templates::Templater.stub!(:new).and_return(@templater)
    end

    def stub_gem_installer
      Gem::Installer.stub!(:new).and_return(mock(Gem::Installer, :unpack => nil))
    end
  end
end