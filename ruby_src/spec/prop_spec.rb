require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/prop'
require 'limelight/styles_builder'
require 'limelight/page'

describe Limelight::Prop do

  before(:each) do
    @illuminator = make_mock("illuminator", :fill_cast => nil)
    @page = Limelight::Page.new(:illuminator => @illuminator)
    @prop = Limelight::Prop.new(:id => "root", :class_name => "root_class")
    @page << @prop
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
    @child1 = Limelight::Prop.new(:id => "child1", :class_name => "child_class")
    @child2 = Limelight::Prop.new(:id => "child2", :class_name => "child_class")
    @grand_child1 = Limelight::Prop.new(:id => "grand_child1", :class_name => "grand_child_class")
    @grand_child2 = Limelight::Prop.new(:id => "grand_child2", :class_name => "grand_child_class")
    @grand_child3 = Limelight::Prop.new(:id => "grand_child3", :class_name => "grand_child_class")
    @grand_child4 = Limelight::Prop.new(:id => "grand_child4", :class_name => "grand_child_class")
    
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
  
  it "should find children by class" do
    build_prop_tree
    @prop.find_by_class("root_class").should == [@prop]
    @prop.find_by_class("child_class").should == [@child1, @child2]
    @prop.find_by_class("grand_child_class").should == [@grand_child1, @grand_child2, @grand_child3, @grand_child4]
  end
  
  it "should get and set text" do
    @prop.text = "blah"
    @prop.text.should == "blah"
  end
  
  it "should have controllers" do
    prop = Limelight::Prop.new(:players => "abc, xyz")
    @page << prop
    prop.players.should == "abc, xyz"
  end
  
  it "should get populated through constructor" do
    prop = Limelight::Prop.new(:class_name => "my_class_name", :id => "123", :players => "a, b, c")
    @page << prop
    
    prop.class_name.should == "my_class_name"
    prop.id.should == "123"
  end
  
  it "should populate styles through constructor" do
    prop = Limelight::Prop.new(:width => "100", :text_color => "white", :background_image => "apple.jpg")
    @page << prop
    
    prop.style.width.should == "100"
    prop.style.text_color.should == "white"
    prop.style.background_image.should == "apple.jpg"
  end
  
  it "should define event through constructor using a string" do
    prop = Limelight::Prop.new(:on_mouse_entered => "return event")
    @page << prop
    
    value = prop.mouse_entered("my event")
    
    value.should == "my event"
  end
  
  it "should pass page on to children" do
    child = Limelight::Prop.new(:class_name => "child")
    
    @prop.parent.should == @page
    @prop.page.should == @page
    
    @prop << child
    child.parent.should == @prop
    child.page.should == @page
  end
  
  it "should set styles upon adding to parent" do
    styles = Limelight::build_styles { child { width 123 } }
    page = Limelight::Page.new(:illuminator => @illuminator, :styles => styles)
    prop = Limelight::Prop.new(:class_name => "child")
    
    page << prop
    
    prop.style.width.should == "123"
  end
  
  it "should set styles upon adding to parent" do
    prop = Limelight::Prop.new(:class_name => "child")
    
    @illuminator.should_receive(:fill_cast).with(prop)
    
    @page << prop
  end
  
  it "should use populate data included by players" do
    prop = Limelight::Prop.new(:class_name => "child", :foo => "bar")
    @illuminator.should_receive(:fill_cast).with(prop) do
      prop.instance_eval "def foo=(value); @foo = value; end; def foo; return @foo; end;"
    end
    
    @page << prop
    
    prop.foo.should == "bar"
  end
  
end

