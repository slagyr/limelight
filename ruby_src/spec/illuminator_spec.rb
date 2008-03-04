require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/illuminator'
require 'limelight/prop_builder'

describe Limelight::Illuminator do

  before(:each) do
    @scene = Limelight::Scene.new(:illuminator => make_mock("casting_director", :fill_cast => nil))
    # @root = Limelight.build_scene(:class_name => "root", :illuminator => make_mock("casting_director", :fill_cast => nil)) do
    #   child do
    #     grandchild
    #   end
    # end
    # @child = @root.children[0]
    # @grandchild = @child.children[0]
    @loader = make_mock("loader")
    @illuminator = Limelight::Illuminator.new(@loader)
  end
  
  def make_root(options={})
    @root = Limelight::Prop.new(options)
    @scene << @root
  end
  
  it "should include default players" do
    prepare_fake_player("root")
    make_root(:class_name => "root")
    
    @root.should_receive(:include_player).with("root module")
    
    @illuminator.fill_cast(@root)
  end
  
  def prepare_fake_player(class_name)
    @loader.should_receive(:exists?).with("players/#{class_name}.rb").and_return(true)
    @loader.should_receive(:path_to).with("players/#{class_name}.rb").and_return("players/#{class_name}.rb")
    Kernel.should_receive(:load).with("players/#{class_name}.rb").and_return(true)
    Object.should_receive(:const_defined?).with(class_name.camalized).and_return(true)
    Object.should_receive(:const_get).with(class_name.camalized).and_return("#{class_name} module")
  end
  
  it "should not load any default players if they don't exist" do
    make_root(:class_name => "root")
    @loader.stub!(:exists?).and_return false
    
    @root.should_not_receive(:include_player)
    @child.should_not_receive(:include_player)
    @grandchild.should_not_receive(:include_player)
    
    @illuminator.fill_cast(@root)
  end
  
  it "should not load any players if they don't define a module with the right name" do
    make_root(:class_name => "root")
    @loader.stub!(:exists?).and_return(true)
    @loader.stub!(:path_to).and_return("blah")
    Kernel.stub!(:load).and_return(true)
    Object.stub!(:const_defined?).and_return(false)
    
    @root.should_not_receive(:include_player)
    
    @illuminator.fill_cast(@root)
  end
  
  it "should load builtin players" do
    @loader.stub!(:exists?).and_return(false)
    make_root(:class_name => "root", :players => "button")
    
    @illuminator.fill_cast(@root)
    
    @root.is_a?(Limelight::Players::Button).should == true
  end
  
  it "should load custom players" do
    @loader.stub!(:exists?).and_return(false)
    prepare_fake_player("custom_player")
    make_root(:class_name => "root", :players => "custom_player")
    
    @root.should_receive(:include_player).with("custom_player module")
    
    @illuminator.fill_cast(@root)
  end
  
  it "should handle multiple players" do
    @loader.stub!(:exists?).and_return(false)
    prepare_fake_player("root")
    prepare_fake_player("custom_player")
    
    make_root(:class_name => "root", :players => "custom_player button")
    
    @root.should_receive(:include_player).with("root module")
    @root.should_receive(:include_player).with("custom_player module")
    @root.should_receive(:include_player).with(Limelight::Players::Button)
    
    @illuminator.fill_cast(@root)
  end

end
