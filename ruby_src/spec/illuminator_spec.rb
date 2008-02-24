require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/illuminator'
require 'limelight/block_builder'

describe Limelight::Illuminator do

  before(:each) do
    @root = Limelight.build_page(:class_name => "root") do
      child do
        grandchild
      end
    end
    @child = @root.children[0]
    @grandchild = @child.children[0]
    @loader = make_mock("loader")
    @illuminator = Limelight::Illuminator.new(@loader)
  end
  
  it "should include default players" do
    prepare_fake_player("root")
    prepare_fake_player("child")
    prepare_fake_player("grandchild")
    
    @root.should_receive(:include_player).with("root module")
    @child.should_receive(:include_player).with("child module")
    @grandchild.should_receive(:include_player).with("grandchild module")
    
    @illuminator.illuminate(@root)
  end
  
  def prepare_fake_player(class_name)
    @loader.should_receive(:exists?).with("players/#{class_name}.rb").and_return(true)
    @loader.should_receive(:path_to).with("players/#{class_name}.rb").and_return("players/#{class_name}.rb")
    Kernel.should_receive(:load).with("players/#{class_name}.rb").and_return(true)
    Object.should_receive(:const_defined?).with(class_name.camalized).and_return(true)
    Object.should_receive(:const_get).with(class_name.camalized).and_return("#{class_name} module")
  end
  
  it "should not load any default players if they don't exist" do
    @loader.stub!(:exists?).and_return false
    
    @root.should_not_receive(:include_player)
    @child.should_not_receive(:include_player)
    @grandchild.should_not_receive(:include_player)
    
    @illuminator.illuminate(@root)
  end
  
  it "should not load any players if they don't define a module with the right name" do
    @loader.stub!(:exists?).and_return(true)
    @loader.stub!(:path_to).and_return("blah")
    Kernel.stub!(:load).and_return(true)
    Object.stub!(:const_defined?).and_return(false)
    
    @root.should_not_receive(:include_player)
    @child.should_not_receive(:include_player)
    @grandchild.should_not_receive(:include_player)
    
    @illuminator.illuminate(@root)
  end
  
  it "should load builtin players" do
    @loader.stub!(:exists?).and_return(false)
    @root.players = "button"
    @child.players = "check_box"
    @grandchild.players = "radio_button"
    
    @illuminator.illuminate(@root)
    
    @root.is_a?(Limelight::Players::Button).should == true
    @child.is_a?(Limelight::Players::CheckBox).should == true
    @grandchild.is_a?(Limelight::Players::RadioButton).should == true
  end
  
  it "should load custom players" do
    @loader.stub!(:exists?).and_return(false)
    prepare_fake_player("custom_player")
    
    @root.players = "custom_player"
    @child.players = "custom_player"
    @grandchild.players = "custom_player"
    
    @root.should_receive(:include_player).with("custom_player module")
    @child.should_receive(:include_player).with("custom_player module")
    @grandchild.should_receive(:include_player).with("custom_player module")
    
    @illuminator.illuminate(@root)
  end
  
  it "should handle multiple players" do
    @loader.stub!(:exists?).and_return(false)
    prepare_fake_player("root")
    prepare_fake_player("child")
    prepare_fake_player("grandchild")
    prepare_fake_player("custom_player")
    
    @root.players = "custom_player button"
    @child.players = "custom_player check_box"
    @grandchild.players = "custom_player radio_button"
    
    @root.should_receive(:include_player).with("root module")
    @child.should_receive(:include_player).with("child module")
    @grandchild.should_receive(:include_player).with("grandchild module")
    
    @root.should_receive(:include_player).with("custom_player module")
    @child.should_receive(:include_player).with("custom_player module")
    @grandchild.should_receive(:include_player).with("custom_player module")
    
    @root.should_receive(:include_player).with(Limelight::Players::Button)
    @child.should_receive(:include_player).with(Limelight::Players::CheckBox)
    @grandchild.should_receive(:include_player).with(Limelight::Players::RadioButton)
    
    @illuminator.illuminate(@root)
  end

end
