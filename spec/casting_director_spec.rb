#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/casting_director'
require 'limelight/prop_builder'

describe Limelight::CastingDirector do

  before(:each) do
    @scene = Limelight::Scene.new(:casting_director => make_mock("casting_director", :fill_cast => nil), :path => "scene_path")
    @loader = make_mock("loader")
    @casting_director = Limelight::CastingDirector.new(@loader)
  end
  
  def make_root(options={})
    @root = Limelight::Prop.new(options)
    @scene << @root
  end
  
  it "should include default players" do
    prepare_fake_player("root")
    make_root(:name => "root")
    
    @root.should_receive(:include_player).with("root module")
    
    @casting_director.fill_cast(@root)
  end
  
  def prepare_fake_player(name)
    @loader.should_receive(:exists?).with("scene_path/players/#{name}.rb").and_return(true)
    @loader.should_receive(:path_to).with("scene_path/players/#{name}.rb").and_return("scene_path/players/#{name}.rb")
    Kernel.should_receive(:load).with("scene_path/players/#{name}.rb").and_return(true)
    Object.should_receive(:const_defined?).with(name.camalized).and_return(true)
    Object.should_receive(:const_get).with(name.camalized).and_return("#{name} module")
  end
  
  it "should not load any default players if they don't exist" do
    make_root(:name => "root")
    @loader.stub!(:exists?).and_return false
    
    @root.should_not_receive(:include_player)
    @child.should_not_receive(:include_player)
    @grandchild.should_not_receive(:include_player)
    
    @casting_director.fill_cast(@root)
  end
  
  it "should not load any players if they don't define a module with the right name" do
    make_root(:name => "root")
    @loader.stub!(:exists?).and_return(true)
    @loader.stub!(:path_to).and_return("blah")
    Kernel.stub!(:load).and_return(true)
    Object.stub!(:const_defined?).and_return(false)
    
    @root.should_not_receive(:include_player)
    
    @casting_director.fill_cast(@root)
  end
  
  it "should load builtin players" do
    @loader.stub!(:exists?).and_return(false)
    make_root(:name => "root", :players => "button")
    
    @casting_director.fill_cast(@root)
    
    @root.is_a?(Limelight::Players::Button).should == true
  end
  
  it "should load custom players" do
    @loader.stub!(:exists?).and_return(false)
    prepare_fake_player("custom_player")
    make_root(:name => "root", :players => "custom_player")
    
    @root.should_receive(:include_player).with("custom_player module")
    
    @casting_director.fill_cast(@root)
  end
  
  it "should handle multiple players" do
    @loader.stub!(:exists?).and_return(false)
    prepare_fake_player("root")
    prepare_fake_player("custom_player")
    
    make_root(:name => "root", :players => "custom_player button")
    
    @root.should_receive(:include_player).with("root module")
    @root.should_receive(:include_player).with("custom_player module")
    @root.should_receive(:include_player).with(Limelight::Players::Button)
    
    @casting_director.fill_cast(@root)
  end

end
