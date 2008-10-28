#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/producer'

describe Limelight::Producer do

  before(:each) do
    TestDir.clean
    Limelight::Production.clear_index
    @root_dir = TestDir.path("test_prod")
    @producer = Limelight::Producer.new(@root_dir)
  end

  it "should have loader on creation" do
    @producer.production.root.root.should == @root_dir
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
    TestDir.create_file("test_prod/props.rb", "child :id => 321")

    scene = @producer.load_props(:path => TestDir.path("test_prod"), :casting_director => make_mock("casting_director", :fill_cast => nil))
    scene.illuminate
    scene.children.size.should == 1
    scene.children[0].name.should == "child"
    scene.children[0].id.should == "321"
  end

  it "should load props even when props.rd doesn't exist." do
    scene = @producer.load_props(:path => TestDir.path("test_prod"), :casting_director => make_mock("casting_director", :fill_cast => nil))
    scene.children.size.should == 0
  end

  it "should load builtin styles" do
    styles = @producer.load_styles(Limelight::Scene.new())

    styles["limelight_builtin_players_combo_box_popup_list"].should_not == nil
  end

  it "should load styles" do
    TestDir.create_file("test_prod/styles.rb", "alpha { width 100 }")
    @producer.builtin_styles = {}

    styles = @producer.load_styles(Limelight::Scene.new(:path => TestDir.path("test_prod")))
    styles.size.should == 1
    styles["alpha"].width.should == "100"
  end

  it "should format prop errors well" do
    TestDir.create_file("test_prod/props.rb", "one\n+\nthree")

    begin
      result = @producer.load_props(:path => TestDir.path("test_prod"), :casting_director => make_mock("casting_director", :fill_cast => nil))
      result.should == nil # should never perform
    rescue Limelight::DSL::BuildException => e
      e.line_number.should == 3
      e.filename.should == TestDir.path("test_prod/props.rb")
      e.message.should include("/props.rb:3: undefined method `+@' for ")
    end
  end

  it "should format styles errors well" do
    TestDir.create_file("test_prod/styles.rb", "one {}\ntwo {}\n-\nthree {}")

    begin
      result = @producer.load_styles(Limelight::Scene.new(:path => TestDir.path("test_prod")))
      result.should == nil # should never perform
    rescue Limelight::DSL::BuildException => e
      e.line_number.should == 4
      e.filename.should == TestDir.path("test_prod/styles.rb")
      e.message.should include("/styles.rb:4: undefined method `-@' for #<Java::LimelightStyles::RichStyle:0x")
    end
  end

  it "should load a stage when stages.rb exists" do
    TestDir.create_file("test_prod/stages.rb", "stage 'Default' do\n default_scene 'abc'\n end")
    @producer.should_receive(:open_scene).with("abc", anything)
    Limelight::Gems.should_receive(:install_gems_in_production)

    @producer.open
  end

  it "should load a scene when stages.rb doesn't exists" do
    @producer.should_not_receive(:open_stages)
    @producer.should_receive(:open_scene).with(:root, anything)
    Limelight::Gems.should_receive(:install_gems_in_production)

    @producer.open
  end

  it "should have one default stage when no stages.rb is provided" do
    @producer.stub!(:open_scene)
    Limelight::Gems.should_receive(:install_gems_in_production)

    @producer.open

    @producer.theater.stages.size.should == 1
    @producer.theater["Limelight"].should_not == nil
  end

  it "should load a stage but not open it if it has no default scene" do
    TestDir.create_file("test_prod/stages.rb", "stage 'Default' do\n default_scene 'abc'\n end\n\nstage 'Hidden' do\n default_scene nil\n end")
    @producer.should_receive(:open_scene).with("abc", anything)
    Limelight::Gems.should_receive(:install_gems_in_production)

    @producer.open
  end

  it "should open a scene" do
    stage = make_mock("stage")
    scene = make_mock("scene")
    @producer.should_receive(:load_props).with(:production => @producer.production, :casting_director => anything, :path => TestDir.path("test_prod/name"), :name => "name").and_return(scene)
    @producer.should_receive(:load_styles).and_return("styles")
    @producer.should_receive(:merge_with_root_styles).with("styles")
    scene.should_receive(:styles=)
    stage.should_receive(:open).with(scene)

    @producer.open_scene("name", stage)
  end

  it "should load empty styles if styles.rb doesn't exist" do
    @producer.builtin_styles = {}

    @producer.load_styles(Limelight::Scene.new(:path => TestDir.path("test_prod"))).should == {}
  end

  it "should use the ProductionBuilder if production.rb is present" do
    TestDir.create_file("test_prod/production.rb", "name 'Fido'")
    @producer.stub!(:open_scene)
    Limelight::Gems.should_receive(:install_gems_in_production)

    @producer.open

    @producer.production.name.should == "Fido"
  end

  it "should load init.rb if it exists" do
    TestDir.create_file("test_prod/production.rb", "name 'Fido'")
    TestDir.create_file("test_prod/init.rb", "")

    @producer.stub!(:open_scene)
    Kernel.should_receive(:load).with(TestDir.path("test_prod/init.rb"))
    Limelight::Gems.should_receive(:install_gems_in_production)

    @producer.open
  end

  it "should not load init.rb when told not to" do
    TestDir.create_file("test_prod/init.rb", "$init_loaded = true")
    Limelight::Gems.should_receive(:install_gems_in_production)

    $init_loaded = false;
    @producer.load(:ignore_init => true)

    $init_loaded.should == false;
  end

  it "should give the same buildin_styles hash twice" do
    @producer.builtin_styles.should_not be(@producer.builtin_styles)
    # Try again
    @producer.builtin_styles.should_not be(@producer.builtin_styles)
  end

end
