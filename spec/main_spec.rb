require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/main'
require 'limelight/util/string_io'

describe Limelight::Main do

  before(:each) do
    @mock_output = Limelight::Util::StringIO.new
    Limelight::Commands.output = @mock_output
  end

  it "should run the specified command" do
    command_class = make_mock("command_class")
    command = make_mock("command")

    Limelight::Commands.should_receive(:[]).with("mock").and_return(command_class)
    command_class.should_receive(:new).and_return(command)
    command.should_receive(:run).with(["1", "2", "3"])

    Limelight::Main.run(["mock", "1", "2", "3"])
  end

  it "should handle --version option" do
    Limelight::Main.run(["--version"])

    @mock_output.should == "Limelight, version #{Limelight::VERSION::STRING}\n"
  end

  it "should print usage" do
    begin
      Limelight::Main.run(["garbage"])
    rescue SystemExit
    end

    @mock_output.should include("Usage: limelight")
  end

end