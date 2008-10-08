require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/commands/create_command'
require 'limelight/templates/production_templater'
require 'limelight/templates/scene_templater'


describe Limelight::Commands::CreateCommand do

  before(:all) do
    @command_class = Limelight::Commands::CreateCommand
  end

  before(:each) do
    @command = @command_class.new
    @command.instance_eval("def usage(e=nil); raise 'Usage called! ' + e + e.backtrace.inspect; end;")
  end

  it "should be listed" do
    Limelight::Commands::LISTING["create"].should == @command_class
  end

  it "should create a production" do
    production_templater = make_mock("production_templater")
    Limelight::Templates::ProductionTemplater.should_receive(:new).with("blah", "default_scene").and_return(production_templater)
    scene_templater = make_mock("scene_templater")
    Limelight::Templates::SceneTemplater.should_receive(:new).with("blah/default_scene").and_return(scene_templater)
    production_templater.should_receive(:generate)
    scene_templater.should_receive(:generate)

    @command.run(["production", "blah"])
  end

  it "should create a scene" do
    Limelight::Templates::ProductionTemplater.should_not_receive(:new)
    scene_templater = make_mock("scene_templater")
    Limelight::Templates::SceneTemplater.should_receive(:new).with("prod/some_scene").and_return(scene_templater)
    scene_templater.should_receive(:generate)

    @command.run(["scene", "prod/some_scene"])
  end

  it "should print useage on invalid template type" do
    @command.should_receive(:usage).at_least(:once)

    @command.run(["blah"])
  end

  it "should print useage on missing paths" do
    @command.should_receive(:usage).at_least(:once)

    @command.run(["production"])
  end

end