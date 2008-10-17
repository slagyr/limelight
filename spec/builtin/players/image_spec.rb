require File.expand_path(File.dirname(__FILE__) + "/../../spec_helper")
require 'limelight/builtin/players/image'
require 'limelight/prop'

describe Limelight::Builtin::Players::Image do

  before(:each) do
#    @scene = Limelight::Scene.new(:casting_director => make_mock("caster", :fill_cast => nil))
    @prop = Limelight::Prop.new
    @prop.include_player(Limelight::Builtin::Players::Image)
  end

  it "should have a ImagePanel" do
    image_panel = @prop.panel.children[0]
    image_panel.class.should == Limelight::UI::Model::ImagePanel
    @prop.image_panel.should be(image_panel)
  end

  it "should have an image" do
    @prop.image = "some/path_to/image.png"
    @prop.panel.children[0].imageFile.should == "some/path_to/image.png";
    @prop.image.should == "some/path_to/image.png";
  end

  it "should have rotation" do
    @prop.rotation = 180
    @prop.rotation.should == 180
    @prop.image_panel.rotation.should == 180
  end

end