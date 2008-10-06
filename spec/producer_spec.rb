#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/producer'

describe Limelight::Producer do
  
  before(:each) do
    Limelight::Production.clear_index
    @producer = Limelight::Producer.new("/tmp")
    @loader = @producer.loader
  end
  
  it "should have loader on creation" do
    @producer.loader.root.should == "/tmp"
  end
  
  it "should take an optional theater on creation" do
    theater = make_mock("theater")
    producer = Limelight::Producer.new("/tmp", theater)
    
    producer.theater.should == theater
  end
  
  it "should build a new theater if none is passing in constructor" do
    @producer.theater.should_not == nil
    @producer.theater.class.should == Limelight::Theater
  end
  
  it "should load props" do
    @loader.should_receive(:exists?).with("./props.rb").and_return(true)
    @loader.should_receive(:load).with("./props.rb").and_return("child :id => 321")
    
    scene = @producer.load_props(".", :casting_director => make_mock("casting_director", :fill_cast => nil))
    scene.children.size.should == 1
    scene.children[0].name.should == "child"
    scene.children[0].id.should == "321"
  end

  it "should load props even when props.rd doesn't exist." do
    @loader.should_receive(:exists?).with("./props.rb").and_return(false)

    scene = @producer.load_props(".", :casting_director => make_mock("casting_director", :fill_cast => nil))
    scene.children.size.should == 0
  end

  it "should load builtin styles" do
    @loader.should_receive(:exists?).with("./styles.rb").and_return(false)

    styles = @producer.load_styles(".")

    styles["limelight_builtin_players_combo_box_popup_list"].should_not == nil
  end
  
  it "should load styles" do
    @producer.builtin_styles = {}
    @loader.should_receive(:exists?).with("./styles.rb").and_return(true)
    @loader.should_receive(:load).with("./styles.rb").and_return("alpha { width 100 }")
    
    styles = @producer.load_styles(".")
    styles.size.should == 1
    styles["alpha"].width.should == "100"
  end
  
  it "should format prop errors well" do
    @loader.should_receive(:exists?).with("./props.rb").and_return(true)
    @loader.should_receive(:load).with("./props.rb").and_return("one\n+\nthree")
    
    begin
      result = @producer.load_props(".", :casting_director => make_mock("casting_director", :fill_cast => nil))
      result.should == nil # should never perform
    rescue Limelight::BuildException => e
      e.line_number.should == 3
      e.filename.should == "./props.rb"
      e.message.should include("./props.rb:3: undefined method `+@' for ")
    end
  end
  
  it "should format styles errors well" do
    @loader.should_receive(:exists?).with("./styles.rb").and_return(true)
    @loader.should_receive(:load).with("./styles.rb").and_return("one {}\ntwo {}\n-\nthree {}")
    
    begin
      result = @producer.load_styles(".")
      result.should == nil # should never perform
    rescue Limelight::BuildException => e
      e.line_number.should == 4
      e.filename.should == "./styles.rb"
      e.message.should include("./styles.rb:4: undefined method `-@' for #<Java::LimelightStyles::RichStyle:0x")
    end
  end
  
  it "should load a stage when stages.rb exists" do
    @loader.should_receive(:exists?).with("production.rb").and_return(false)
    @loader.should_receive(:exists?).with("init.rb").and_return(false)
    @loader.should_receive(:exists?).with("stages.rb").and_return true
    @producer.should_receive(:load_stages).and_return([make_mock("stage", :default_scene => "abc", :name => "Default")])
    @producer.should_receive(:open_scene).with("abc", anything)
    
    @producer.open
  end
  
  it "should load a scene when stages.rb doesn't exists" do
    @loader.should_receive(:exists?).with("production.rb").and_return(false)
    @loader.should_receive(:exists?).with("init.rb").and_return(false)
    @loader.should_receive(:exists?).with("stages.rb").and_return false
    @producer.should_not_receive(:open_stages)
    @producer.should_receive(:open_scene).with("/tmp", anything)
    
    @producer.open
  end
  
  it "should have one default stage when no stages.rb is provided" do
    @loader.should_receive(:exists?).with("production.rb").and_return(false)
    @loader.should_receive(:exists?).with("init.rb").and_return(false)
    @loader.should_receive(:exists?).with("stages.rb").and_return false
    @producer.stub!(:open_scene)
    
    @producer.open
    
    @producer.theater.stages.size.should == 1
    @producer.theater["Limelight"].should_not == nil
  end
  
  it "should open a scene" do
    stage = make_mock("stage")
    scene = make_mock("scene")
    @producer.should_receive(:load_styles).and_return("styles")
    @producer.should_receive(:merge_with_root_styles).with("styles")
    @producer.should_receive(:load_props).with("some/path", :styles => "styles", :production => @producer.production, :casting_director => anything, :loader => @loader, :path => "some/path", :name => "path").and_return(scene)
    stage.should_receive(:open).with(scene)
    
    @producer.open_scene("some/path", stage)
  end
  
  it "should load empty styles if styles.rb doesn't exist" do
    @producer.builtin_styles = {}
    @loader.should_receive(:exists?).with("./styles.rb").and_return(false)
    
    @producer.load_styles(".").should == {}
  end

  it "should use the ProductionBuilder if production.rb is present" do
    @loader.should_receive(:exists?).with("production.rb").and_return(true)
    @loader.should_receive(:exists?).with("init.rb").and_return(false)
    @loader.should_receive(:load).with("production.rb").and_return("name 'Fido'")
    @loader.should_receive(:exists?).with("stages.rb").and_return(false)
    @producer.stub!(:open_scene)

    @producer.open

    @producer.production.name.should == "Fido"
  end
  
  it "should load init.rb if it exists" do
    @loader.should_receive(:exists?).with("production.rb").and_return(true)
    @loader.should_receive(:exists?).with("init.rb").and_return(true)
    @loader.should_receive(:load).with("production.rb").and_return("name 'Fido'")
    @loader.should_receive(:exists?).with("stages.rb").and_return(false)
    @producer.stub!(:open_scene)
    Kernel.should_receive(:load).with("/tmp/init.rb")
    
    @producer.open
  end
  
end
