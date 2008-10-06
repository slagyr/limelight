require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/main'

describe Limelight::Main do

  it "should run the specified command" do
    command_class = make_mock("command_class")
    command = make_mock("command")

    Limelight::Commands.should_receive(:[]).with("mock").and_return(command_class)
    command_class.should_receive(:new).and_return(command)
    command.should_receive(:run).with(["1", "2", "3"])

    Limelight::Main.run(["mock", "1", "2", "3"])
  end

end