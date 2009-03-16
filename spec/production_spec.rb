#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/production'

describe Limelight::Production, "Instance methods" do

  before(:each) do
    @producer = make_mock("producer")
    @theater = make_mock("theater")
    @production = Limelight::Production.new("/tmp")
    @production.producer = @producer
    @production.theater = @theater
  end
  
  it "should know it path, producer, and theater" do
    @production.producer.should == @producer
    @production.theater.should == @theater
    @production.path.should == "/tmp"
  end
  
  it "should be indexed" do
    Limelight::Production[@production.name].should == @production
  end
  
  it "should raise an error when setting the name to a duplicate name" do
    @production.name = "Bill"
    
    production = Limelight::Production.new("/tmp")
    lambda { production.name = "Bill" }.should raise_error(Limelight::LimelightException, "Production name 'Bill' is already taken")
  end

  it "should know it's init file" do
    @production.init_file.should == "/tmp/init.rb"
  end

  it "should know it's stages file" do
    @production.stages_file.should == "/tmp/stages.rb"
  end

  it "should know it's styles file" do
    @production.styles_file.should == "/tmp/styles.rb"
  end

  it "should know it's gems directory" do
    @production.gems_directory.should == "/tmp/__resources/gems"
  end

  it "should provide paths to it's scenes" do
    @production.scene_directory("one").should == "/tmp/one"
    @production.scene_directory("two").should == "/tmp/two"
    @production.scene_directory(:root).should == "/tmp"
  end

end

describe Limelight::Production, "Class methods" do
  
  class TestProduction
    attr_accessor :name
  end
  
  before(:each) do
    Limelight::Production.clear_index
    @production = TestProduction.new
  end
  
  it "should add productions to the index" do
    @production.name = "Bob"
    
    Limelight::Production.index(@production)
    
    Limelight::Production["Bob"].should == @production
  end
  
  it "should give a production a name if it doesn't have one" do
    Limelight::Production.index(@production)
    
    @production.name.should == "1"
    Limelight::Production["1"].should == @production
    
    production2 = TestProduction.new
    Limelight::Production.index(production2)
    
    production2.name.should == "2"
    Limelight::Production["2"].should == production2
  end
  
  it "should raise an error if adding a duplicate name" do
    production2 = TestProduction.new
    
    @production.name = "Bob"
    production2.name = "Bob"
    
    Limelight::Production.index(@production)
    lambda { Limelight::Production.index(production2) }.should raise_error(Limelight::LimelightException, "Production name 'Bob' is already taken")
  end
  
end
