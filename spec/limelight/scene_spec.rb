#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/scene'
require 'limelight/dsl/styles_builder'

describe Limelight::Scene do

  before(:each) do
    @casting_director = mock("casting_director", :fill_cast => nil)
    @scene = Limelight::Scene.new(:casting_director => @casting_director)
  end

  it "should have a styles hash" do
    @scene.illuminate
    @scene.styles_store.should_not == nil
    @scene.styles_store.size.should == 0
  end

  it "should have a button group cache" do
    @scene.button_groups.should_not == nil
    @scene.button_groups.class.should == Limelight::UI::ButtonGroupCache
  end

  it "should pullout casting_director from options" do
    scene = Limelight::Scene.new(:casting_director => @casting_director)
    scene.illuminate

    scene.casting_director.should == @casting_director
  end

  it "adds on_scene_opened actions" do
    action = Proc.new { puts "I should never get printed" }
    @scene.on_scene_opened &action

    actions = @scene.panel.event_handler.get_actions(Limelight::UI::Events::SceneOpenedEvent)
    actions.contains(action).should == true
  end

  it "should have a cast" do
    @scene.cast.should_not == nil
    @scene.cast.is_a?(Module).should == true
  end

  it "should have a cast" do
    @scene.cast.should_not == nil
    @scene.cast.is_a?(Module).should == true
  end

  it "should have an acceptible path when none is provided" do
    Limelight::Scene.new().path.should == File.expand_path("")
  end

  it "should get styles from the Java model" do
    java_model = @scene.panel
    @scene.styles_store.should be(java_model.styles_store)
  end

#  it "should set the production during illumination before casting" do
#    production = mock("production")
#    scene = Limelight::Scene.new(:styles_hash => "styles", :casting_director => @casting_director, :production => production)
#    @casting_director.should_receive(:fill_cast).with(scene) do |scene|
#      scene.production.should_not == nil
#    end
#
#    scene.illuminate
#  end

  describe Limelight::Scene, "paths" do

    before(:each) do
      @scene = Limelight::Scene.new(:path => "/tmp")
    end

    it "should know it's props file" do
      @scene.props_file.should == "/tmp/props.rb"
    end

    it "should know it's styles file" do
      @scene.styles_file.should == "/tmp/styles.rb"
    end

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

    it "should unindex child's prop" do
      prop = Limelight::Prop.new(:id => "some_id")
      child = Limelight::Prop.new(:id => "child_id")
      prop.children << child

      @scene << child
      @scene << prop

      @scene.unindex_prop(prop)

      @scene.find("some_id").should == nil
      @scene.find("child_id").should == nil
    end

    it "should not blow up if child has no id" do
      prop = Limelight::Prop.new(:id => "some_id")
      child = Limelight::Prop.new(:id => nil)
      prop.children << child

      @scene << child
      @scene << prop

      @scene.unindex_prop(prop)

      @scene.find("some_id").should == nil
    end

    it "should unindex grandchildren props" do
      prop = Limelight::Prop.new(:id => "some_id")
      child = Limelight::Prop.new(:id => "child_id")
      grandchild = Limelight::Prop.new(:id => "grandchild_id")
      child.children << grandchild
      prop.children << child

      @scene << grandchild
      @scene << child
      @scene << prop

      @scene.unindex_prop(prop)

      @scene.find("grandchild_id").should == nil
    end

    it "should convert ids to string when finding" do
      prop = Limelight::Prop.new(:id => 123)
      @scene << prop

      @scene.find(123).should == prop
    end
  end

end
