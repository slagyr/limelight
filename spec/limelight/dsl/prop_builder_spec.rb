#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../spec_helper")
require 'limelight/dsl/prop_builder'

describe Limelight::DSL::PropBuilder do

  before(:each) do
    @caster = mock("caster", :fill_cast => nil)
    @scene = Limelight::Scene.new(:name => "root", :casting_director => @caster)
  end

  it "should build root" do
    root = Limelight.build_props(@scene)
    root.illuminate

    root.class.should == Limelight::Scene
    root.name.should == "root"
    root.panel.should_not == nil
    root.children.size.should == 0
  end

  it "should build one child prop" do
    root = Limelight::build_props(@scene) do
      child
    end
    root.illuminate

    root.children.size.should == 1
    child = root.children[0]
    child.class.should == Limelight::Prop
    child.name.should == "child"
    child.panel.should_not == nil
    child.children.size.should == 0
  end

  it "should allow multiple children" do
    root = Limelight::build_props(@scene) do
      child1
      child2
    end
    root.illuminate

    root.children.size.should == 2
    root.children[0].name.should == "child1"
    root.children[1].name.should == "child2"
  end

  it "should allow nested children" do
    root = Limelight::build_props(@scene) do
      child do
        grandchild
      end
    end
    root.illuminate

    root.children.size.should == 1
    root.children[0].name.should == "child"
    root.children[0].children.size.should == 1
    root.children[0].children[0].name.should == "grandchild"
  end

  it "should be able to set the id" do
    root = Limelight::build_props(@scene) do
      child :id => "child_1", :players => "x, y, z"
    end
    root.illuminate

    child = root.children[0]
    child.id.should == "child_1"
    child.players.should == "x, y, z"
  end

  it "should allow setting styles" do
    root = Limelight::build_props(@scene) do
      child :width => "100", :font_size => "10", :top_border_color => "blue"
    end
    root.illuminate

    child = root.children[0]
    child.style.width.should == "100"
    child.style.font_size.should == "10"
    child.style.top_border_color.should == "#0000ffff"
  end

  it "should allow defining events through constructor" do
    root = Limelight::build_props(@scene) do
      child :on_mouse_clicked => "$RECIPIENT = self"
    end
    root.illuminate

    child = root.children[0]
    mouse.click(child)
    $RECIPIENT.should == child
  end

  it "should allow scene configuration" do
    root = Limelight::build_props(@scene) do
      __ :name => "root", :id => "123"
    end
    root.illuminate

    root.children.size.should == 0
    root.name.should == "root"
    root.id.should == "123"
  end

  it "should give every prop their scene" do
    root = Limelight::build_props(@scene) do
      child do
        grandchild
      end
    end

    root.scene.should == root
    root.children[0].scene.should == root
    root.children[0].children[0].scene.should == root
  end

  it "should install external props" do
    loader = mock("loader", :exists? => true)
    loader.should_receive(:load).with("external.rb").and_return("child :id => 123")

    root = Limelight::build_props(@scene, :id => 321, :build_loader => loader, :casting_director => @caster) do
      __install "external.rb"
    end
    root.illuminate

    root.id.should == "321"
    root.children.size.should == 1
    child = root.children[0]
    child.name.should == "child"
    child.id.should == "123"
  end

  it "should fail if no loader is provided" do
    begin
      root = Limelight::build_props(@scene, :id => 321, :build_loader => nil) do
        __install "external.rb"
      end
      root.should == nil # should never get here
    rescue Exception => e
      e.message.should == "Cannot install external props because no loader was provided"
    end
  end

  it "should fail when the external file doesn't exist" do
    loader = mock("loader")
    loader.should_receive(:exists?).with("external.rb").and_return(false)

    begin
      root = Limelight::build_props(@scene, :id => 321, :build_loader => loader) do
        __install "external.rb"
      end
    rescue Exception => e
      e.message.should == "External prop file: 'external.rb' doesn't exist"
    end
  end

  it "should fail with PropException when there's problem in the external file" do
    loader = mock("loader", :exists? => true)
    loader.should_receive(:load).with("external.rb").and_return("+")

    begin
      root = Limelight::build_props(@scene, :id => 321, :build_loader => loader) do
        __install "external.rb"
      end
      "exception should have been thrown".should == nil?
    rescue Limelight::DSL::BuildException => e
      e.message.include?("external.rb:1: (eval):1:").should == true
      e.message.include?("unexpected end-of-file").should == true
    end
  end

  it "should build onto an existing block" do
    prop = Limelight::Prop.new
    scene = Limelight::Scene.new(:casting_director => mock(:casting_director, :fill_cast => nil))
    scene << prop
    builder = Limelight::DSL::PropBuilder.new(prop)
    block = Proc.new { one; two { three } }
    builder.instance_eval(& block)
    scene.illuminate

    prop.children.length.should == 2
    prop.children[0].name.should == "one"
    prop.children[1].name.should == "two"
    prop.children[1].children.length.should == 1
    prop.children[1].children[0].name.should == "three"
  end

  it "should not crash with prop named display" do
    root = Limelight::build_props(@scene) do
      display :id => "display"
    end
    root.illuminate

    root.children.size.should == 1
    display = root.children[0]
    display.name.should == "display"
    display.id.should == "display"
  end

  it "should allow instance variables" do
    root = Limelight::build_props(@scene, :instance_variables => {:id1 => "abc", :id2 => "xyz"}) do
      __ :id => @id1
      child :id => @id2
    end
    root.illuminate

    root.id.should == "abc"
    child = root.children[0]
    child.id.should == "xyz"
  end

  it "should propogate instance variables to nested levels" do
    root = Limelight::build_props(@scene, :instance_variables => {:name => "blah"}) do
      child :id => "child", :name => @name do
        grand_child :id => "grand_child", :name => @name do
          great_grand_child :id => "great_grand_child", :name => @name do
            baby :id => "baby", :name => @name
          end
        end
      end
    end
    root.illuminate

    root.find("child").name.should == "blah"
    root.find("grand_child").name.should == "blah"
    root.find("great_grand_child").name.should == "blah"
    root.find("baby").name.should == "blah"
  end

  it "should allow instance_variable when installing an external props file" do
    loader = mock("loader", :exists? => true)
    loader.should_receive(:load).with("external.rb").and_return("child :id => @desired_id")

    root = Limelight::build_props(@scene, :id => 321, :build_loader => loader, :casting_director => @caster) do
      __install "external.rb", :desired_id => "123"
    end
    root.illuminate

    child = root.children[0]
    child.name.should == "child"
    child.id.should == "123"
  end

  it "should allow defining methods inside dsl" do
    root = Limelight::build_props(@scene, :instance_variables => {:name => "blah"}) do
      def foo(value)
        foo_prop do
          __ :text => value if !block_given?
          yield if block_given?
        end
      end

      foo("first") do
        foo("child")
      end
      foo("second")
    end
    root.illuminate

    root.children[0].name.should == "foo_prop"
    root.children[0].children[0].name.should == "foo_prop"
    root.children[0].children[0].text.should == "child"
    root.children[1].name.should == "foo_prop"
    root.children[1].text.should == "second"
  end

end