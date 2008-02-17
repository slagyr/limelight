require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/block'

describe Limelight::Block do

  before(:each) do
    @block = Limelight::Block.new(:id => "root", :class_name => "root_class")
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
    @block.id = "123"
    @block.id.should == "123"
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
  
  it "should allow removal of children" do
    build_block_tree
    
    @block.remove(@child1)
    
    @block.children.length.should == 1
    @block.children[0].should be(@child2)
    @block.panel.is_child(@child1.panel).should == false
    @child1.parent.should == nil
  end

end

