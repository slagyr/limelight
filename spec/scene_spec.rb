#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/scene'

describe Limelight::Scene do

  before(:each) do
    @casting_director = make_mock("casting_director", :fill_cast => nil)
    @scene = Limelight::Scene.new(:casting_director => @casting_director)
  end

  it "should have a styles hash" do
    @scene.illuminate
    @scene.styles.should_not == nil
    @scene.styles.size.should == 0
  end

  it "should have a button group cache" do
    @scene.button_groups.should_not == nil
    @scene.button_groups.class.should == Limelight::UI::ButtonGroupCache
  end

  it "should pullout sytles and casting_director from options" do
    scene = Limelight::Scene.new(:styles => "styles", :casting_director => @casting_director)
    scene.illuminate

    scene.styles.should == "styles"
    scene.casting_director.should == @casting_director
  end

  it "should have opened event" do
    @scene.should respond_to(:scene_opened)
  end

  it "should have a cast" do
    @scene.cast.should_not == nil
    @scene.cast.is_a?(Module).should == true
  end

  describe Limelight::Scene, "Prop Indexing" do

    before(:each) do
      @scene.illuminate
    end

    it "should index props" do
      prop = Limelight::Prop.new(:id => "some_id")
      @scene << prop

      @scene.find("some_id").should == prop
    end

    it "should raise error if indexing prop with duplicate id" do
      prop1 = Limelight::Prop.new(:id => "some_id")
      prop2 = Limelight::Prop.new(:id => "some_id")
      @scene << prop1

      lambda { @scene << prop2 }.should raise_error(Limelight::LimelightException, "Duplicate id: some_id")
    end

    it "should unindex prop" do
      prop = Limelight::Prop.new(:id => "some_id")
      @scene << prop
      @scene.unindex_prop(prop)

      @scene.find("some_id").should == nil
    end

    it "should convert ids to string when finding" do
      prop = Limelight::Prop.new(:id => 123)
      @scene << prop

      @scene.find(123).should == prop
    end
  end

end
