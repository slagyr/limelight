#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/scene'

describe Limelight::Scene do

  before(:each) do
    @casting_director = make_mock("casting_director", :fill_cast => nil)
    @scene = Limelight::Scene.new(:casting_director => @casting_director)
  end

  it "should have a styles hash" do
    @scene.illuminate
    @scene.styles.should_not == nil
    @scene.styles.size.should == 0
  end

  it "should have a button group cache" do
    @scene.button_groups.should_not == nil
    @scene.button_groups.class.should == Limelight::UI::ButtonGroupCache
  end

  it "should pullout sytles and casting_director from options" do
    scene = Limelight::Scene.new(:styles => "styles", :casting_director => @casting_director)
    scene.illuminate

    scene.styles.should == "styles"
    scene.casting_director.should == @casting_director
  end

  it "should have opened event" do
    @scene.should respond_to(:scene_opened)
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
  
  context "Prop extensions" do
  
    context "#text_for prop" do
      before do
        @scene.illuminate
        prop = Limelight::Prop.new(:id => "prop",:text => "maproom")

        @scene << prop
      end
    
      it "returns text for a prop" do
        @scene.text_for("prop").should == "maproom"
      end
    
      it "returns nil if prop not found" do
        @scene.text_for("no_existy").should be_nil
      end
    end
    
    context "#set_text_for" do
       before do
          @scene.illuminate
          prop = Limelight::Prop.new(:id => "prop")

          @scene << prop
        end
        
        it "sets the text on a prop" do
          @scene.set_text_for("prop", "new_text")
          @scene.find("prop").text.should == "new_text"
        end
    end
  
    context "#remove_children_of" do
      before do
        @scene.illuminate
      end
      it "calls remove_all on the prop" do
        prop = mock('prop')
        @scene.stub!(:find).and_return(prop)
        prop.should_receive(:remove_all)
        @scene.remove_children_of('foo')
      end
    
      it "does it on the appropriate prop" do
        prop = mock('prop', :remove_all => nil)
        @scene.should_receive(:find).with('prop_name').and_return(prop)
        @scene.remove_children_of('prop_name')
      end
    
      it "does nothing if it can't find the prop" do
        @scene.remove_children_of(nil)
      end
    end
  end
end
