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
    Limelight::Studio.index(@production)
    Limelight::Studio[@production.name].should == @production
  end
  
  it "should raise an error when setting the name to a duplicate name" do
    @production.name = "Bill"
    Limelight::Studio.index(@production)
    
    production = Limelight::Production.new("/tmp")
    Limelight::Studio.index(production)
    lambda { production.name = "Bill" }.should raise_error(Limelight::LimelightException, "Production name 'Bill' is already taken")
  end

  it "should get it's name from the file" do
    Limelight::Production.new("/tmp").name.should == "tmp"
    Limelight::Production.new("/Somewhere/over/the/rainbow").name.should == "rainbow"
    Limelight::Production.new("my_name/is/kid").name.should == "kid"   
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

  it "should allow close by default" do
    @production.allow_close?.should == true
  end

  it "should tell studio it closed and triger it's closing events" do
    @production.should_receive(:production_closing)
    Limelight::Studio.should_receive(:production_closed).with(@production)
    @production.should_receive(:production_closed)

    @production.close
  end

end


