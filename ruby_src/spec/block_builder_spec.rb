require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/block_builder'

describe Limelight::PageBuilder do

  before(:each) do
  end
  
  it "should build root" do
    root = Limelight.build_page(:class_name => "root")
    
    root.class.should == Limelight::Page
    root.class_name.should == "root"
    root.panel.should_not == nil
    root.children.size.should == 0
  end
  
  it "should build one child block" do
    root = Limelight::build_page(:class_name => "root") do
      child
    end
    
    root.children.size.should == 1
    child = root.children[0]
    child.class.should == Limelight::Block
    child.class_name.should == "child"
    child.panel.should_not == nil
    child.children.size.should == 0
  end
  
  it "should allow multiple children" do
    root = Limelight::build_page(:class_name => "root") do
      child1
      child2
    end
    
    root.children.size.should == 2
    root.children[0].class_name.should == "child1"
    root.children[1].class_name.should == "child2"
  end

  it "should allow nested children" do
    root = Limelight::build_page(:class_name => "root") do
      child do
        grandchild
      end
    end

    root.children.size.should == 1
    root.children[0].class_name.should == "child"
    root.children[0].children.size.should == 1
    root.children[0].children[0].class_name.should == "grandchild"
  end
  
  it "should be able to set the id" do
    root = Limelight::build_page(:class_name => "root") do
      child :id => "child_1", :players => "x, y, z"
    end
    
    child = root.children[0]
    child.id.should == "child_1"
    child.players.should == "x, y, z"
  end
  
  it "should allow setting styles" do
    root = Limelight::build_page(:class_name => "root") do
      child :width => "100", :font_size => "10", :top_border_color => "blue"
    end
    
    child = root.children[0]
    child.style.width.should == "100"
    child.style.font_size.should == "10"
    child.style.top_border_color.should == "blue"
  end
  
  it "should allow defining events through constructor" do
    root = Limelight::build_page(:class_name => "root") do
      child :on_mouse_entered => "return [self, event]"
    end  
    
    child = root.children[0]
    child.mouse_entered("blah").should == [child, "blah"]
  end

end