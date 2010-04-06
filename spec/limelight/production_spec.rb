#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/production'

describe Limelight::Production, "Instance methods" do

  before(:each) do
    @producer = mock("producer")
    @theater = mock("theater")
    @studio = Limelight::Studio.install
    @production = Limelight::Production.new("/tmp")
    @production.producer = @producer
    @production.theater = @theater
  end

  it "should know it path, producer, and theater" do
    @production.producer.should == @producer
    @production.theater.should == @theater
    @production.path.should == "/tmp"
  end

  it "should get it's name from the file" do
    Limelight::Production.new("/tmp").name.should == "tmp"
    Limelight::Production.new("/Somewhere/over/the/rainbow").name.should == "rainbow"
    Limelight::Production.new("my_name/is/kid").name.should == "kid"
  end

  it "should know its init file" do
    @production.init_file.should == "/tmp/init.rb"
  end

  it "should know its stages file" do
    @production.stages_file.should == "/tmp/stages.rb"
  end

  it "should know its styles file" do
    @production.styles_file.should == "/tmp/styles.rb"
  end

  it "should know its gems directory" do
    @production.gems_directory.should == "/tmp/__resources/gems/gems"
  end
  
  it "should know its gems root" do
    @production.gems_root.should == "/tmp/__resources/gems"
  end

  it "should provide paths to it's scenes" do
    @production.scene_directory("one").should == "/tmp/one"
    @production.scene_directory("two").should == "/tmp/two"
    @production.scene_directory(:root).should == "/tmp"
  end

  it "should allow close by default" do
    @production.allow_close?.should == true
  end

  it "should tell producer to do the closing" do
    @producer.should_receive(:close)

    @production.close
  end

  it "should publish on drb" do
    @producer.should_receive(:publish_production_on_drb).with(9000)
    @production.publish_on_drb(9000)
  end

  it "should handle empty theater" do
    @production.should_receive(:allow_close?).at_least(1).and_return(true)
    @production.should_receive(:close)

    @production.theater_empty!
  end

  describe "with files" do

    after(:each) do
      TestDir.clean
    end
    
    it "should load it's root styles" do
      TestDir.create_file("test_prod/styles.rb", "a_style { width 100; height 200 }")
      @production = Limelight::Production.new( TestDir.path("test_prod"))

      styles = @production.root_styles
      styles["a_style"].width.should == "100"
      styles["a_style"].height.should == "200"
      @production.root_styles.should be(styles)
    end
  end

end


