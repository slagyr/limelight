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
  
  it "should allow page configuration" do
    root = Limelight::build_page do
      __ :class_name => "root", :id => "123"
    end
    
    root.children.size.should == 0
    root.class_name.should == "root"
    root.id.should == "123"
  end
  
  it "should give every block their page" do
    root = Limelight::build_page(:class_name => "root") do
      child do
        grandchild
      end
    end
    
    root.page.should == root
    root.children[0].page.should == root
    root.children[0].children[0].page.should == root
  end

  it "should install external blocks" do
    loader = make_mock("loader", :exists? => true)
    loader.should_receive(:load).with("external.rb").and_return("child :id => 123")
    
    root = Limelight::build_page(:id => 321, :loader => loader) do
      __install "external.rb"
    end  
    
    root.id.should == 321
    root.children.size.should == 1
    child = root.children[0]
    child.class_name.should == "child"
    child.id.should == 123
  end
  
  it "should fail if no loader is provided" do
    begin
      root = Limelight::build_page(:id => 321, :loader => nil) do
        __install "external.rb"
      end
      root.should == nil # should never get here
    rescue Exception => e
      e.message.should == "Cannot install external blocks because no loader was provided"
    end
  end
  
  it "should fail when the external file doesn't exist" do
    loader = make_mock("loader")
    loader.should_receive(:exists?).with("external.rb").and_return(false)
    
    begin
      root = Limelight::build_page(:id => 321, :loader => loader) do
        __install "external.rb"
      end
    rescue Exception => e
      e.message.should == "External block file: 'external.rb' doesn't exist"
    end
  end
  
  it "should fail with BlockException when there's problem in the external file" do
    loader = make_mock("loader", :exists? => true)
    loader.should_receive(:load).with("external.rb").and_return("+")
    
    begin
      root = Limelight::build_page(:id => 321, :loader => loader ) do
        __install "external.rb"
      end  
    rescue Limelight::BuildException => e
      e.message.should == "external.rb:1: (eval):1: , unexpected end-of-file\n\n\t1: +\n"
    end
  end
  
end