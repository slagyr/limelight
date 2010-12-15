#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/scene'
require 'limelight/dsl/styles_builder'

describe Limelight::Scene do

  before(:each) do
    @scene = Limelight::Scene.new()
  end

  it "should have a styles hash" do
    @scene.styles.should_not == nil
    @scene.styles.size.should == 0
  end

  it "should have a button group cache" do
    @scene.button_groups.should_not == nil
    @scene.button_groups.class.should == Java::limelight.ui.ButtonGroupCache
  end

  it "has a casting diretor" do
    @scene.casting_director.should_not == nil
  end

  it "adds on_scene_opened actions" do
    action = Proc.new { puts "I should never get printed" }
    @scene.on_scene_opened &action

    actions = @scene.peer.event_handler.get_actions(Java::limelight.ui.events.panel.SceneOpenedEvent)
    actions.contains(action).should == true
  end

  it "should have an acceptible path when none is provided" do
    Limelight::Scene.new().path.should == ""
  end

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
      @scene.peer.illuminate
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

      begin
        @scene << prop2
      rescue Exception => e
        e.message.should == "limelight.LimelightException: Duplicate id: some_id"
      end
    end

    it "should unindex prop" do
      prop = Limelight::Prop.new(:id => "some_id")
      @scene << prop
      @scene.remove(prop)

      @scene.find("some_id").should == nil
    end

    it "should unindex child's prop" do
      prop = Limelight::Prop.new(:id => "some_id")
      child = Limelight::Prop.new(:id => "child_id")
      prop << child

      @scene << prop

      @scene.remove(prop)

      @scene.find("some_id").should == nil
      @scene.find("child_id").should == nil
    end

    it "should not blow up if child has no id" do
      prop = Limelight::Prop.new(:id => "some_id")
      child = Limelight::Prop.new(:id => nil)
      prop << child

      @scene << prop

      @scene.remove(prop)

      @scene.find("some_id").should == nil
    end

    it "should unindex grandchildren props" do
      prop = Limelight::Prop.new(:id => "some_id")
      child = Limelight::Prop.new(:id => "child_id")
      grandchild = Limelight::Prop.new(:id => "grandchild_id")
      child << grandchild
      prop << child

      @scene << prop

      @scene.remove(prop)

      @scene.find("grandchild_id").should == nil
    end

    it "should convert ids to string when finding" do
      prop = Limelight::Prop.new(:id => 123)
      @scene << prop

      @scene.find(123).should == prop
    end
  end

end
