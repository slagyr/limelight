#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/prop'
require 'limelight/dsl/styles_builder'
require 'limelight/scene'
require 'limelight/production'

describe Limelight::Prop do

  before(:each) do
    @player_recruiter = mock("player_recruiter", :recruitPlayer => nil, :canRecruit => false)
    @scene = Limelight::Scene.new(:player_recruiter => @player_recruiter)
    @prop = Limelight::Prop.new(:id => "root", :name => "root_class")
    @scene.peer.illuminate
    @scene << @prop
  end

  it "have an id" do
    @prop.peer.illuminate
    @prop.id.should == "root"
  end

  def build_prop_tree
    @child1 = Limelight::Prop.new(:id => "child1", :name => "child_class")
    @child2 = Limelight::Prop.new(:id => "child2", :name => "child_class")
    @grand_child1 = Limelight::Prop.new(:id => "grand_child1", :name => "grand_child_class")
    @grand_child2 = Limelight::Prop.new(:id => "grand_child2", :name => "grand_child_class")
    @grand_child3 = Limelight::Prop.new(:id => "grand_child3", :name => "grand_child_class")
    @grand_child4 = Limelight::Prop.new(:id => "grand_child4", :name => "grand_child_class")

    @prop << @child1 << @child2
    @child1 << @grand_child1 << @grand_child2
    @child2 << @grand_child3 << @grand_child4
  end

  it "find children by id" do
    build_prop_tree
    @scene.find("blah").should == nil
    @scene.find("root").should be(@prop)
    @scene.find("child1").should be(@child1)
    @scene.find("child2").should be(@child2)
    @scene.find("grand_child1").should be(@grand_child1)
    @scene.find("grand_child2").should be(@grand_child2)
    @scene.find("grand_child3").should be(@grand_child3)
    @scene.find("grand_child4").should be(@grand_child4)
  end

  it "find children by name" do
    build_prop_tree
    @prop.find_by_name("root_class").should == [@prop]
    @prop.find_by_name("child_class").should == [@child1, @child2]
    @prop.find_by_name("grand_child_class").should == [@grand_child1, @grand_child2, @grand_child3, @grand_child4]
  end

  it "get and set text" do
    @prop.text = "blah"
    @prop.text.should == "blah"
    @prop.text = 123
    @prop.text.should == "123"
    @prop.text = nil
    @prop.text.should == ""
  end

  it "get populated through constructor" do
    prop = Limelight::Prop.new(:name => "my_name", :id => "123", :players => "a, b, c")
    @scene << prop

    prop.name.should == "my_name"
    prop.id.should == "123"
  end

  it "populate styles through constructor" do
    prop = Limelight::Prop.new(:width => "100", :text_color => "white", :background_image => "apple.jpg")
    @scene << prop

    prop.style.width.should == "100"
    prop.style.text_color.should == "#ffffffff"
    prop.style.background_image.should == "apple.jpg"
  end

  it "define event through constructor using a string" do
    prop = Limelight::Prop.new(:on_mouse_clicked => "$RESULT = self")
    @scene << prop

    Java::limelight.ui.events.panel.MouseClickedEvent.new(0, nil, 0).dispatch(prop.peer)

    $RESULT.should == prop
  end

  it "pass scene on to children" do
    child = Limelight::Prop.new(:name => "child")

    @prop.parent.should == @scene
    @prop.scene.should == @scene

    @prop << child
    child.parent.should == @prop
    child.scene.should == @scene
  end

  it "set styles upon illuminating, and convert them to strings" do
    prop = Limelight::Prop.new(:width => "100", :height => 200, :horizontal_alignment => :center)

    @scene << prop

    prop.style.width.should == "100"
    prop.style.height.should == "200"
    prop.style.horizontal_alignment.should == "center"
  end

  it "be able to remove children" do
    child1 = Limelight::Prop.new()
    child2 = Limelight::Prop.new()
    child3 = Limelight::Prop.new()
    @prop << child1 << child2 << child3

    @prop.remove(child2)

    @prop.children.length.should == 2
    @prop.children.include?(child2).should == false
    @prop.peer.children.length.should == 2
    @prop.peer.children.include?(child2.peer).should == false
  end

  it "make dimensions accessible" do
    @prop.peer.should_receive(:get_bounds).and_return("whole area")
    @prop.peer.should_receive(:get_bordered_bounds).and_return("area inside borders")
    @prop.peer.should_receive(:get_padded_bounds).and_return("area inside padding")

    @prop.bounds.should == "whole area"
    @prop.bordered_bounds.should == "area inside borders"
    @prop.padded_bounds.should == "area inside padding"
  end

  it "provides location" do
    @prop.peer.should_receive(:get_location).and_return("location")
    @prop.peer.should_receive(:get_absolute_location).and_return("absolute location")

    @prop.location.should == "location"
    @prop.absolute_location.should == "absolute location"
  end

  it "give you a pen" do
    graphics = mock("graphics", :setColor => nil, :setStroke => nil, :setRenderingHint => nil)
    @prop.peer.should_receive(:getGraphics).and_return(graphics)

    pen = @prop.pen

    pen.context.should be(graphics)
  end

  it "set after paint action" do
    block = Proc.new { |pen|}

    @prop.after_painting &block

    action = @prop.peer.after_paint_action
    action.should_not == nil
    action.class.should == Limelight::PaintAction
    action.block.should == block
  end

  it "clear after paint action" do
    @prop.after_painting { |pen| puts "blah" }

    @prop.after_painting nil

    @prop.peer.after_paint_action.should == nil
  end

  it "build children" do
    @prop.scene.production = Limelight::Production.new(Java::limelight.model.FakeProduction.new("some/path"))
    @prop.build do
      one
      two do
        three
      end
    end

    @prop.children.length.should == 2
    @prop.children[0].name.should == "one"
    @prop.children[1].name.should == "two"
    @prop.children[1].children.length.should == 1
    @prop.children[1].children[0].name.should == "three"
  end

  it "build children with options" do
    @prop.scene.production = Limelight::Production.new(Java::limelight.model.FakeProduction.new("some/path"))
    @prop.build(:one_val => "hello") do
      one :text => @one_val
      two do
        three
      end

    end

    @prop.children.length.should == 2
    @prop.children[0].name.should == "one"
    @prop.children[0].text.should == "hello"
    @prop.children[1].name.should == "two"
    @prop.children[1].children.length.should == 1
    @prop.children[1].children[0].name.should == "three"
  end

  it "plays sound" do
    @fs = Java::limelight.io.FakeFileSystem.installed
    @fs.create_text_file("/path/to/prod/some.au", "blah")

    production = Limelight::Production.new(Java::limelight.model.FakeProduction.new("/path/to/prod"))
    @scene.production = production

    @prop.peer.should_receive(:play_sound).with("/path/to/prod/some.au")
    @prop.play_sound("some.au")
  end

  it "gets players" do
    @prop.players.should == @prop.peer.getPlayers
  end

  it "gets stagehands" do
    @prop.stagehands.should_not == nil
    @prop.stagehands.should be(@prop.stagehands)
    @prop.stagehands.class.should == Limelight::Util::MapHash
    @prop.stagehands.map.should == @prop.peer.getStagehands
  end

  #TODO remove_all should not remove scrollbars

  describe "events" do

    [{:method => :on_mouse_pressed, :klass => Java::limelight.ui.events.panel.MousePressedEvent},
     {:method => :on_mouse_released, :klass => Java::limelight.ui.events.panel.MouseReleasedEvent},
     {:method => :on_mouse_clicked, :klass => Java::limelight.ui.events.panel.MouseClickedEvent},
     {:method => :on_mouse_moved, :klass => Java::limelight.ui.events.panel.MouseMovedEvent},
     {:method => :on_mouse_dragged, :klass => Java::limelight.ui.events.panel.MouseDraggedEvent},
     {:method => :on_mouse_entered, :klass => Java::limelight.ui.events.panel.MouseEnteredEvent},
     {:method => :on_mouse_exited, :klass => Java::limelight.ui.events.panel.MouseExitedEvent},
     {:method => :on_mouse_wheel, :klass => Java::limelight.ui.events.panel.MouseWheelEvent},
     {:method => :on_key_pressed, :klass => Java::limelight.ui.events.panel.KeyPressedEvent},
     {:method => :on_key_released, :klass => Java::limelight.ui.events.panel.KeyReleasedEvent},
     {:method => :on_char_typed, :klass => Java::limelight.ui.events.panel.CharTypedEvent},
     {:method => :on_focus_gained, :klass => Java::limelight.ui.events.panel.FocusGainedEvent},
     {:method => :on_focus_lost, :klass => Java::limelight.ui.events.panel.FocusLostEvent},
     {:method => :on_button_pushed, :klass => Java::limelight.ui.events.panel.ButtonPushedEvent},
     {:method => :on_value_changed, :klass => Java::limelight.ui.events.panel.ValueChangedEvent},
     {:method => :on_illuminated, :klass => Java::limelight.ui.events.panel.IlluminatedEvent},
    ].each do |event|
      it "adds #{event[:method]} actions" do
        action = Proc.new { puts "I should never get printed" }
        @prop.send(event[:method], & action)

        actions = @prop.peer.event_handler.get_actions(event[:klass])
        actions.contains(action).should == true
      end
    end

  end

  describe "id" do

    it "index its id when illuminated" do
      @scene.find("root").should == @prop
    end

    it "unindex ids when removing children" do
      child = Limelight::Prop.new(:id => "child")
      @prop << child
      @scene.find("child").should == child

      @prop.remove(child)

      @scene.find("child").should == nil
    end

    it "unindex ids when removing all children" do
      child1 = Limelight::Prop.new(:id => "child1")
      child2 = Limelight::Prop.new(:id => "child2")
      @prop << child1 << child2

      @prop.remove_all

      @scene.find("child1").should == nil
      @scene.find("child2").should == nil
    end

    it "unindex grandchildren on remove all" do
      grandchild = Limelight::Prop.new(:id => "grandchild")
      child = Limelight::Prop.new(:id => "child")
      child << grandchild
      @prop << child

      @prop.remove_all

      @scene.find("child").should == nil
      @scene.find("grandchild").should == nil
    end

    it "index it's id when being re-added to the prop tree" do
      child = Limelight::Prop.new(:id => "child")
      @prop << child
      @prop.remove(child)
      @prop << child

      @scene.find("child").should == child
    end

    it "traverses and index children when being re-added to the prop tree" do
      grandchild = Limelight::Prop.new(:id => "grandchild")
      child = Limelight::Prop.new(:id => "child")
      child << grandchild
      @prop << child
      @prop.remove(child)
      @prop << child

      @scene.find("grandchild").should == grandchild
    end


  end

  describe "launch" do

    it "launch a url" do
      os = mock("Limelight OS")
      instance = mock("Limelight OS instance", :os => os)
      Java::limelight.Context.stub!(:instance).and_return(instance)

      os.should_receive(:launch).with("http://www.google.com")

      @prop.launch("http://www.google.com")
    end

  end

end
