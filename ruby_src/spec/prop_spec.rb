require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/prop'
require 'limelight/styles_builder'
require 'limelight/scene'

describe Limelight::Prop do

  before(:each) do
    @casting_director = make_mock("casting_director", :fill_cast => nil)
    @scene = Limelight::Scene.new(:casting_director => @casting_director)
    @prop = Limelight::Prop.new(:id => "root", :name => "root_class")
    @scene << @prop
  end
  
  it "should extend added controllers and invoke the extended hook" do
    module TestController
      class << self
        attr_reader :extended_prop
        def extended(prop)
          @extended_prop = (prop)
        end
      end
      
      def test_method
      end
    end
    
    @prop.add_controller(TestController)
    
    TestController.extended_prop.should == @prop
    @prop.respond_to?(:test_method).should == true
  end
  
  it "should have an id" do
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
  
  it "should find children by id" do
    build_prop_tree
    @prop.find("blah").should == nil
    @prop.find("root").should be(@prop)
    @prop.find("child1").should be(@child1)
    @prop.find("child2").should be(@child2)
    @prop.find("grand_child1").should be(@grand_child1)
    @prop.find("grand_child2").should be(@grand_child2)
    @prop.find("grand_child3").should be(@grand_child3)
    @prop.find("grand_child4").should be(@grand_child4)
  end
  
  it "should find children by name" do
    build_prop_tree
    @prop.find_by_name("root_class").should == [@prop]
    @prop.find_by_name("child_class").should == [@child1, @child2]
    @prop.find_by_name("grand_child_class").should == [@grand_child1, @grand_child2, @grand_child3, @grand_child4]
  end
  
  it "should get and set text" do
    @prop.text = "blah"
    @prop.text.should == "blah"
  end
  
  it "should have controllers" do
    prop = Limelight::Prop.new(:players => "abc, xyz")
    @scene << prop
    prop.players.should == "abc, xyz"
  end
  
  it "should get populated through constructor" do
    prop = Limelight::Prop.new(:name => "my_name", :id => "123", :players => "a, b, c")
    @scene << prop
    
    prop.name.should == "my_name"
    prop.id.should == "123"
  end
  
  it "should populate styles through constructor" do
    prop = Limelight::Prop.new(:width => "100", :text_color => "white", :background_image => "apple.jpg")
    @scene << prop
    
    prop.style.width.should == "100"
    prop.style.text_color.should == "white"
    prop.style.background_image.should == "apple.jpg"
  end
  
  it "should define event through constructor using a string" do
    prop = Limelight::Prop.new(:on_mouse_entered => "return event")
    @scene << prop
    
    value = prop.mouse_entered("my event")
    
    value.should == "my event"
  end
  
  it "should pass scene on to children" do
    child = Limelight::Prop.new(:name => "child")
    
    @prop.parent.should == @scene
    @prop.scene.should == @scene
    
    @prop << child
    child.parent.should == @prop
    child.scene.should == @scene
  end
  
  it "should set styles upon adding to parent" do
    styles = Limelight::build_styles { child { width 123 } }
    scene = Limelight::Scene.new(:casting_director => @casting_director, :styles => styles)
    prop = Limelight::Prop.new(:name => "child")
    
    scene << prop
    
    prop.style.width.should == "123"
  end
  
  it "should set styles upon adding to parent" do
    prop = Limelight::Prop.new(:name => "child")
    
    @casting_director.should_receive(:fill_cast).with(prop)
    
    @scene << prop
  end
  
  it "should use populate data included by players" do
    prop = Limelight::Prop.new(:name => "child", :foo => "bar")
    @casting_director.should_receive(:fill_cast).with(prop) do
      prop.instance_eval "def foo=(value); @foo = value; end; def foo; return @foo; end;"
    end
    
    @scene << prop
    
    prop.foo.should == "bar"
  end
  
  it "should be able to remove children" do
    child1 = Limelight::Prop.new()
    child2 = Limelight::Prop.new()
    child3 = Limelight::Prop.new()
    @prop << child1 << child2 << child3
    
    @prop.remove(child2)
    
    @prop.children.length.should == 2
    @prop.children.should_not include(child2)
    @prop.panel.components.length.should == 2
    @prop.panel.components.should_not include(child2.panel)
  end
  
  it "should make dimensions accessible" do
    @prop.panel.should_receive(:get_rectangle).and_return("whole area")
    @prop.panel.should_receive(:get_rectangle_inside_borders).and_return("area inside borders")
    
    @prop.area.should == "whole area"
    @prop.bordered_area.should == "area inside borders"
  end
  
  it "should give you a pen" do
    graphics = make_mock("graphics", :setColor => nil, :setStroke => nil, :setRenderingHint => nil)
    @prop.panel.should_receive(:getGraphics).and_return(graphics)
    
    pen = @prop.pen
    
    pen.context.should be(graphics)
  end
  
end

