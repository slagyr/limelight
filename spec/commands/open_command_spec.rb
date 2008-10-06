require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/commands/open_command'
require 'limelight/producer'

describe Limelight::Commands::OpenCommand do

  before(:all) do
    @command_class = Limelight::Commands::OpenCommand
    @command = @command_class.new
  end

  it "should be listed" do
    Limelight::Commands::LISTING["open"].should == @command_class
  end

  it "should open a production" do
    Limelight::Main.should_receive(:initialize_context)
    Limelight::Producer.should_receive(:open).with("production_name")

    @command.run(["production_name"])
  end

  it "should open the default production" do
    Limelight::Main.should_receive(:initialize_context)
    Limelight::Producer.should_receive(:open).with(@command_class::DEFAULT_PRODUCTION)

    @command.run([])
  end

end