#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/producer'

describe Limelight::Producer do

  before(:each) do
    TestDir.clean
    Limelight::Studio.uninstall
    @root_dir = TestDir.path("test_prod")
    @producer = Limelight::Producer.new(@root_dir)
    Limelight::Studio.install
  end

  it "should have loader on creation" do
    @producer.production.root.root.should == @root_dir
  end

  it "should take an optional theater on creation" do
    theater = mock("theater")
    producer = Limelight::Producer.new("/tmp", theater)

    producer.theater.should == theater
  end

  it "should build a new theater if none is passing in constructor" do
    @producer.theater.should_not == nil
    @producer.theater.class.should == Limelight::Theater
  end

  it "should load props" do
    TestDir.create_file("test_prod/props.rb", "child :id => 321")

    scene = @producer.load_props(:path => TestDir.path("test_prod"), :casting_director => mock("casting_director", :fill_cast => nil))
    scene.illuminate
    scene.children.size.should == 1
    scene.children[0].name.should == "child"
    scene.children[0].id.should == "321"
  end

  it "should load props even when props.rd doesn't exist." do
    scene = @producer.load_props(:path => TestDir.path("test_prod"), :casting_director => mock("casting_director", :fill_cast => nil))
    scene.children.size.should == 0
  end

  it "should load builtin styles" do
    styles = {}
    @producer.load_styles("blah", styles)

    styles["limelight_builtin_combo_box_popup_list"].should_not == nil
  end

  it "should load styles" do
    TestDir.create_file("test_prod/styles.rb", "alpha { width 100 }")
    Limelight::Producer.stub!(:builtin_styles).and_return({})

    styles = {}
    @producer.load_styles("test_prod/styles.rb", styles)
    styles.size.should == 1
    styles["alpha"].width.should == "100"
  end

  # Broken in JRuby 1.3
  #
  #  it "should format prop errors well" do
  #    TestDir.create_file("test_prod/props.rb", "one\n+\nthree")
  #
  #    begin
  #      result = @producer.load_props(:path => TestDir.path("test_prod"), :casting_director => mock("casting_director", :fill_cast => nil))
  #      result.should == nil # should never perform
  #    rescue Limelight::DSL::BuildException => e
  #      e.line_number.should == 3
  #      e.filename.should == TestDir.path("test_prod/props.rb")
  #      e.message.include?("/props.rb:3: undefined method `+@' for ").should == true
  #    end
  #  end
  #
  #  it "should format styles errors well" do
  #    TestDir.create_file("test_prod/styles.rb", "one {}\ntwo {}\n-\nthree {}")
  #
  #    begin
  #      result = @producer.load_styles(Limelight::Scene.new(:path => TestDir.path("test_prod")))
  #      result.should == nil # should never perform
  #    rescue Limelight::DSL::BuildException => e
  #      e.line_number.should == 4
  #      e.filename.should == TestDir.path("test_prod/styles.rb")
  #      e.message.include?("/styles.rb:4: undefined method `-@' for #<Java::LimelightStyles::RichStyle:0x").should == true
  #    end
  #  end

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

  it "should not load a scene when stages.rb doesn't exists and production indicates no default_scene" do
    @producer.should_not_receive(:open_stages)
    @producer.should_not_receive(:open_scene)
    @producer.production.should_receive(:default_scene).and_return(nil)
    Limelight::Gems.should_receive(:install_gems_in_production)

    @producer.open
  end

  it "should load a stage but not open it if it has no default scene" do
    TestDir.create_file("test_prod/stages.rb", "stage 'Default' do\n default_scene 'abc'\n end\n\nstage 'Hidden' do\n default_scene nil\n end")
    @producer.should_receive(:open_scene).with("abc", anything)
    Limelight::Gems.should_receive(:install_gems_in_production)

    @producer.open
  end

  it "should open a scene" do
    stage = mock("stage")
    scene = mock("scene", :styles_file => "blah", :styles_store => {})
    @producer.should_receive(:load_props).with(:production => @producer.production, :casting_director => anything, :path => TestDir.path("test_prod/name"), :name => "name").and_return(scene)
    @producer.should_receive(:load_styles)
    stage.should_receive(:open).with(scene)

    @producer.open_scene("name", stage)
  end

  it "should load empty styles if styles.rb doesn't exist" do
    Limelight::Producer.stub!(:builtin_styles).and_return({})

    styles = {}
    @producer.load_styles(TestDir.path("test_prod/styles.rb"), styles)

    styles.should == {}
  end

  it "should extend the production if production.rb is present" do
    TestDir.create_file("test_prod/production.rb", "module Production; def name; return 'Fido'; end; def foo; end; end;")
    @producer.stub!(:open_scene)

    @producer.establish_production

    @producer.production.name.should == "Fido"
    @producer.production.respond_to?(:foo).should == true
  end

  it "should load init.rb if it exists" do
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

  it "should call production_opening, production_loaded, production_opened when opening a production" do
    @producer.stub!(:open_scene)
    Limelight::Gems.should_receive(:install_gems_in_production)
    production = @producer.production
    production.should_receive(:production_opening)
    production.should_receive(:production_loaded)
    production.should_receive(:production_opened)

    @producer.open
  end

  it "should check limelight version" do
    @producer.production.should_receive(:minimum_limelight_version).and_return("0.0.0")
    @producer.version_compatible?.should == true

    @producer.production.should_receive(:minimum_limelight_version).and_return("999.0.0")
    @producer.version_compatible?.should == false
  end

  it "should allow options such as instance variables to be passed to open_scene" do
    stage = mock("stage")
    scene = mock("scene", :styles_file => "blah", :styles_store => {})
    @producer.should_receive(:load_props).with(:instance_variables => { :foo => "bar" }, :production => @producer.production, :casting_director => anything, :path => TestDir.path("test_prod/name"), :name => "name").and_return(scene)
    @producer.should_receive(:load_styles)
    stage.should_receive(:open).with(scene)

    @producer.open_scene("name", stage, :instance_variables => { :foo => "bar" })
  end
  
  it "should give the same buildin_styles hash twice" do
    Limelight::Producer.builtin_styles.should be(Limelight::Producer.builtin_styles)
    Limelight::Producer.builtin_styles["limelight_builtin_curtains"].should_not == nil
  end

  it "should close a production" do
    theater = mock("theater")
    production = @producer.production
    production.theater = theater
    production.should_receive(:closed=).with(true)
    production.should_receive(:production_closing)
    theater.should_receive(:close)
    production.should_receive(:production_closed)
    Limelight::Context.instance.studio.should_receive(:production_closed).with(production)

    close_thread = @producer.close
    close_thread.join()
  end

  it "should publish a production on drb" do
    service = mock("service")
    DRb.should_receive(:start_service).with("druby://localhost:9000", @producer.production).and_return(service)

    @producer.publish_production_on_drb(9000)
  end

  it "should stop the drb service when closing the production" do
    service = mock("service")
    DRb.stub!(:start_service).with("druby://localhost:9000", @producer.production).and_return(service)
    @producer.publish_production_on_drb(9000)

    service.should_receive(:stop_service)
    @producer.close.join
  end
  
end
