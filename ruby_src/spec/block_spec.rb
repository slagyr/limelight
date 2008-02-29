require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/block'
require 'limelight/styles_builder'
require 'limelight/page'

describe Limelight::Block do

  before(:each) do
    @illuminator = make_mock("illuminator", :fill_cast => nil)
    @page = Limelight::Page.new(:illuminator => @illuminator)
    @block = Limelight::Block.new(:id => "root", :class_name => "root_class")
    @page << @block
  end
  
  it "should extend added controllers and invoke the extended hook" do
    module TestController
      class << self
        attr_reader :extended_block
        def extended(block)
          @extended_block = (block)
        end
      end
      
      def test_method
      end
    end
    
    @block.add_controller(TestController)
    
    TestController.extended_block.should == @block
    @block.respond_to?(:test_method).should == true
  end
  
  it "should have an id" do
    @block.id.should == "root"
  end
  
  def build_block_tree
    @child1 = Limelight::Block.new(:id => "child1", :class_name => "child_class")
    @child2 = Limelight::Block.new(:id => "child2", :class_name => "child_class")
    @grand_child1 = Limelight::Block.new(:id => "grand_child1", :class_name => "grand_child_class")
    @grand_child2 = Limelight::Block.new(:id => "grand_child2", :class_name => "grand_child_class")
    @grand_child3 = Limelight::Block.new(:id => "grand_child3", :class_name => "grand_child_class")
    @grand_child4 = Limelight::Block.new(:id => "grand_child4", :class_name => "grand_child_class")
    
    @block << @child1 << @child2
    @child1 << @grand_child1 << @grand_child2
    @child2 << @grand_child3 << @grand_child4
  end
  
  it "should find children by id" do
    build_block_tree
    @block.find("blah").should == nil
    @block.find("root").should be(@block)
    @block.find("child1").should be(@child1)
    @block.find("child2").should be(@child2)
    @block.find("grand_child1").should be(@grand_child1)
    @block.find("grand_child2").should be(@grand_child2)
    @block.find("grand_child3").should be(@grand_child3)
    @block.find("grand_child4").should be(@grand_child4)
  end
  
  it "should find children by class" do
    build_block_tree
    @block.find_by_class("root_class").should == [@block]
    @block.find_by_class("child_class").should == [@child1, @child2]
    @block.find_by_class("grand_child_class").should == [@grand_child1, @grand_child2, @grand_child3, @grand_child4]
  end
  
  it "should get and set text" do
    @block.text = "blah"
    @block.text.should == "blah"
  end
  
  it "should have controllers" do
    block = Limelight::Block.new(:players => "abc, xyz")
    @page << block
    block.players.should == "abc, xyz"
  end
  
  it "should get populated through constructor" do
    block = Limelight::Block.new(:class_name => "my_class_name", :id => "123", :players => "a, b, c")
    @page << block
    
    block.class_name.should == "my_class_name"
    block.id.should == "123"
  end
  
  it "should populate styles through constructor" do
    block = Limelight::Block.new(:width => "100", :text_color => "white", :background_image => "apple.jpg")
    @page << block
    
    block.style.width.should == "100"
    block.style.text_color.should == "white"
    block.style.background_image.should == "apple.jpg"
  end
  
  it "should define event through constructor using a string" do
    block = Limelight::Block.new(:on_mouse_entered => "return event")
    @page << block
    
    value = block.mouse_entered("my event")
    
    value.should == "my event"
  end
  
  it "should pass page on to children" do
    child = Limelight::Block.new(:class_name => "child")
    
    @block.parent.should == @page
    @block.page.should == @page
    
    @block << child
    child.parent.should == @block
    child.page.should == @page
  end
  
  it "should set styles upon adding to parent" do
    styles = Limelight::build_styles { child { width 123 } }
    page = Limelight::Page.new(:illuminator => @illuminator, :styles => styles)
    block = Limelight::Block.new(:class_name => "child")
    
    page << block
    
    block.style.width.should == "123"
  end
  
  it "should set styles upon adding to parent" do
    block = Limelight::Block.new(:class_name => "child")
    
    @illuminator.should_receive(:fill_cast).with(block)
    
    @page << block
  end
  
  it "should use populate data included by players" do
    block = Limelight::Block.new(:class_name => "child", :foo => "bar")
    @illuminator.should_receive(:fill_cast).with(block) do
      block.instance_eval "def foo=(value); @foo = value; end; def foo; return @foo; end;" 
    end
    
    @page << block
    
    block.foo.should == "bar"
  end
  
end

