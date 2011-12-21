#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/player_recruiter'
require 'limelight/dsl/prop_builder'
require 'limelight/production'

describe Limelight::PlayerRecruiter do

  before(:each) do
    @fs = Java::limelight.io.FakeFileSystem.installed
    @scene = Limelight::Scene.new(:path => "scene_path", :player_recruiter => mock("player_recruiter", :recruit_player => nil))
    @production = Limelight::Production.new(Java::limelight.model.FakeProduction.new("production_path"))
    @scene.production = @production
    @player_recruiter = Limelight::PlayerRecruiter.new()
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

    @player_recruiter.can_recruit?("root", "scene_path/players").should == true

    player = @player_recruiter.recruit_player("root", "scene_path/players")

    player.path.should == "scene_path/players/root.rb"
    player.name.should == "root"
    @player_recruiter.cast::Root.should == player
  end

  it "includes player with non-snake-name" do
    prepare_player("scene_path", "root_beer")
    make_root(:name => "root")

    @player_recruiter.can_recruit?("root-beer", "scene_path/players").should == true
    player = @player_recruiter.recruit_player("root-beer", "scene_path/players")

    player.path.should == "scene_path/players/root_beer.rb"
    player.name.should == "root-beer"
    @player_recruiter.cast::RootBeer.should == player
  end

  it "creates the player in the scene's cast and doesn't currupt the global namespace" do
    prepare_player("scene_path", "root")
    make_root(:name => "root")

    @player_recruiter.recruit_player("root", "scene_path/players")

    Object.const_defined?("Root").should == false
    @player_recruiter.cast.const_defined?("Root").should == true
  end

  it "doesn't load any players if they don't exist" do
    make_root(:name => "root")

    @player_recruiter.can_recruit?("root", "scene_path/players").should == false
  end

  it "doesn't reload known players in a scene" do
    prepare_player("scene_path", "root")

    make_root(:name => "root")
    player = @player_recruiter.recruit_player("root", "scene_path/players")

    prepare_player("scene_path", "root", "def foo; end;")
    make_root(:name => "root")
    new_player = @player_recruiter.recruit_player("root", "scene_path/players")

    player.should == new_player
    @player_recruiter.cast::Root.should == player
  end

end
