#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require "limelight/gems"
require 'limelight/production'

describe Limelight::Gems do

  before(:each) do
    @production = Limelight::Production.new(TestDir.path("prod"))
  end

  it "should know the load path" do
    Limelight::Gems.load_path.should be($:)
    Limelight::Gems.current_production = @production
  end

  it "add a gem to load path" do
    path = []
    Limelight::Gems.stub!(:load_path).and_return(path)

    Limelight::Gems.install("gem_dir_name", ["lib"])

    path.length.should == 1
    path[0].should == File.join(@production.gems_directory, "gem_dir_name/lib")
  end

  it "add a gem to the beginning of the load path" do
    path = ["foo"]
    Limelight::Gems.stub!(:load_path).and_return(path)

    Limelight::Gems.install("gem_dir_name", ["lib"])

    path.length.should == 2
    path[0].should == File.join(@production.gems_directory, "gem_dir_name/lib")
    path[1].should == "foo"
  end

  it "add a gem to the beginning of the load path in the same order provided" do
    path = ["foo"]
    Limelight::Gems.stub!(:load_path).and_return(path)

    Limelight::Gems.install("gem_dir_name", ["lib", "src", "ruby"])

    path.length.should == 4
    path[0].should == File.join(@production.gems_directory, "gem_dir_name/lib")
    path[1].should == File.join(@production.gems_directory, "gem_dir_name/src")
    path[2].should == File.join(@production.gems_directory, "gem_dir_name/ruby")
    path[3].should == "foo"
  end

  describe "finding gems" do

    before(:each) do
      TestDir.clean
    end

    it "should load all the gems in a production" do
      TestDir.create_file("prod/__resources/gems/gems/gem1/limelight_init.rb", "Limelight::Gems.install('gem1', ['fee'])")
      TestDir.create_file("prod/__resources/gems/gems/gem2/limelight_init.rb", "Limelight::Gems.install('gem2', ['fie'])")
      TestDir.create_file("prod/__resources/gems/gems/gem3/limelight_init.rb", "Limelight::Gems.install('gem3', ['foe', 'fum'])")
      path = []
      Limelight::Gems.stub!(:load_path).and_return(path)

      Limelight::Gems.install_gems_in_production(@production)

      path.length.should == 4
      path[0].should == File.join(@production.gems_directory, "gem1/fee")
      path[1].should == File.join(@production.gems_directory, "gem2/fie")
      path[2].should == File.join(@production.gems_directory, "gem3/foe")
      path[3].should == File.join(@production.gems_directory, "gem3/fum")
    end

    it "should gracefully handle missing gems dir" do
      TestDir.establish_dir("prod")

      path = []
      Limelight::Gems.stub!(:load_path).and_return(path)
      Limelight::Gems.install_gems_in_production(@production)

      path.length.should == 0
    end
    
    it "should set the gem home and gem path to the __resources/gems so that using the gem command within frozen gems will work" do
      Gem.should_receive(:use_paths).with(@production.gems_root, Gem.default_path)
      
      Limelight::Gems.install_gems_in_production(@production)
    end
  end

end
