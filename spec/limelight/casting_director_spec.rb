#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/casting_director'
require 'limelight/dsl/prop_builder'
require 'limelight/production'
require 'limelight/file_loader'

describe Limelight::CastingDirector do

  before(:each) do
    TestDir.clean
    $casted_props = []
    $casted_players = []
    @scene = Limelight::Scene.new(:casting_director => mock("casting_director", :fill_cast => nil), :path => TestDir.path("scene_path"))
    @scene.illuminate
    @loader = Limelight::FileLoader.for_root(TestDir.root)
    @casting_director = Limelight::CastingDirector.new(@loader)
  end
  
  def make_root(options={})
    @root = Limelight::Prop.new(options)
    @scene << @root
  end

  def prepare_player(location, name, src = nil)
    if src == nil
      src = "module #{name.camalized}; def self.extended(prop); $casted_props << prop; $casted_players << self; end; end;"
    end
    TestDir.create_file("#{location}/players/#{name}.rb", src)
  end
  
  it "should include default players" do
    prepare_player("scene_path", "root")
    make_root(:name => "root")
    
    @casting_director.fill_cast(@root)

    $casted_props.length.should == 1
    $casted_props[0].should == @root
    $casted_players[0].name.include?("Root").should == true
    @root.is_a?($casted_players[0]).should == true
  end

  it "should not creat the player in the scene's cast and not currupt the global namespace" do
    prepare_player("scene_path", "root")
    make_root(:name => "root")

    @casting_director.fill_cast(@root)

    Object.const_defined?("Root").should == false
    @scene.cast.const_defined?("Root").should == true
  end
  
  it "should not load any default players if they don't exist" do
    make_root(:name => "root")
    @root.should_not_receive(:include_player)
    
    @casting_director.fill_cast(@root)

    $casted_props.length.should == 0
    $casted_players.length.should == 0
  end
  
  it "should not load any players if they don't define a module with the right name" do
    make_root(:name => "root")
    prepare_player("scene_path", "root", "# empty file")
    @root.should_not_receive(:include_player)

    @casting_director.fill_cast(@root)

    $casted_props.length.should == 0
    $casted_players.length.should == 0
  end

  it "should load builtin players" do
    make_root(:name => "root", :players => "button")

    @casting_director.fill_cast(@root)

    @root.is_a?(Limelight::Builtin::Players::Button).should == true
  end

  it "should load custom players" do
    prepare_player("scene_path", "custom_player")
    make_root(:name => "root", :players => "custom_player")

    @casting_director.fill_cast(@root)

    $casted_props[0].should == @root
    $casted_players[0].name.include?("CustomPlayer").should == true
    @root.is_a?($casted_players[0]).should == true
  end       

  it "should handle multiple players" do
    prepare_player("scene_path", "root")
    prepare_player("scene_path", "custom_player")

    make_root(:name => "root", :players => "custom_player button")

    @casting_director.fill_cast(@root)

    $casted_props[0].should == @root
    $casted_players[0].name.include?("Root").should == true
    @root.is_a?($casted_players[0]).should == true
    @root.is_a?(Limelight::Builtin::Players::Button).should == true
    $casted_players[1].name.include?("CustomPlayer").should == true
    @root.is_a?($casted_players[1]).should == true
  end

  it "should load shared custom players" do
    production = Limelight::Production.new(TestDir.root)
    @scene.production = production

    prepare_player("scene_path", "root")
    prepare_player(".", "shared_player")
    make_root(:name => "root", :players => "shared_player")

    @casting_director.fill_cast(@root)

    $casted_props[0].should == @root
    $casted_players[0].name.include?("Root").should == true
    $casted_players[1].name.include?("SharedPlayer").should == true
    @root.is_a?($casted_players[0]).should == true
    @root.is_a?($casted_players[1]).should == true
  end

  it "should not allow casting without a scene" do
    lambda { @casting_director.fill_cast(Limelight::Prop.new) }.should raise_error(Limelight::LimelightException, "Cannot cast a Prop without a Scene.")
  end

  it "should not reload known players in a scene" do
    prepare_player("scene_path", "root")

    make_root(:name => "root")
    @casting_director.fill_cast(@root)
    first_root_player = $casted_players[0]

    prepare_player("scene_path", "root", "module Root; def foo; end; end;")
    make_root(:name => "root")
    @casting_director.fill_cast(@root)

    @root.should_not respond_to(:foo)
    @root.is_a?(first_root_player).should == true
  end

  it "should not reload known players in a scene even with a new casting director" do
    prepare_player("scene_path", "root")

    make_root(:name => "root")
    @casting_director.fill_cast(@root)
    first_root_player = $casted_players[0]

    @casting_director = Limelight::CastingDirector.new(@loader)
    prepare_player("scene_path", "root", "module Root; def foo; end; end;")
    make_root(:name => "root")
    @casting_director.fill_cast(@root)

    @root.should_not respond_to(:foo)
    @root.is_a?(first_root_player).should == true
  end

end
