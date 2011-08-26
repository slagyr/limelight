#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/casting_director'
require 'limelight/dsl/prop_builder'
require 'limelight/production'

describe Limelight::CastingDirector do

  before(:each) do
    @fs = Java::limelight.io.FakeFileSystem.installed
    $casted_props = []
    @scene = Limelight::Scene.new(:path => "scene_path", :casting_director => mock("casting_director", :cast_player => nil))
    @production = Limelight::Production.new(Java::limelight.model.FakeProduction.new("production_path"))
    @scene.production = @production
    @casting_director = Limelight::CastingDirector.new()
  end
  
  def make_root(options={})
    @root = Limelight::Prop.new(options)
    @scene << @root
  end

  def prepare_player(location, name, src = nil)
    if src == nil
      src = "on_cast { $casted_props << self }"
    end
    @fs.create_text_file("#{location}/players/#{name}.rb", src)
  end
  
  it "includes one player" do
    prepare_player("scene_path", "root")
    make_root(:name => "root")
    
    @casting_director.has_player("root", "scene_path/players").should == true
    
    @casting_director.cast_player(@root, "root", "scene_path/players")

    $casted_props.length.should == 1
    $casted_props[0].should == @root
    @root.is_a?(@casting_director.cast::Root).should == true
  end

  it "creates the player in the scene's cast and doesn't currupt the global namespace" do
    prepare_player("scene_path", "root")
    make_root(:name => "root")

    @casting_director.cast_player(@root, "root", "scene_path/players")

    Object.const_defined?("Root").should == false
    @casting_director.cast.const_defined?("Root").should == true
  end
  
  it "doesn't load any players if they don't exist" do
    make_root(:name => "root")
    
    @casting_director.has_player("root", "scene_path/players").should == false
  end

  it "should handle multiple players" do
    prepare_player("scene_path", "root")
    prepare_player("scene_path", "custom_player")

    @casting_director.cast_player(@root, "root", "scene_path/players")
    @casting_director.cast_player(@root, "custom_player", "scene_path/players")

    $casted_props[0].should == @root
    @root.is_a?(@casting_director.cast::Root).should == true
    @root.is_a?(@casting_director.cast::CustomPlayer).should == true
  end

  it "should not reload known players in a scene" do
    prepare_player("scene_path", "root")

    make_root(:name => "root")
    @casting_director.cast_player(@root, "root", "scene_path/players")
    first_root_player = @casting_director.cast::Root

    prepare_player("scene_path", "root", "def foo; end;")
    make_root(:name => "root")
    @casting_director.cast_player(@root, "root", "scene_path/players")

    @root.should_not respond_to(:foo)
    @root.is_a?(first_root_player).should == true
  end

end
