require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/commands/unpack_command'
require 'limelight/util/string_io'

describe Limelight::Commands::UnpackCommand do

  before(:all) do
    @command_class = Limelight::Commands::UnpackCommand
    @command = @command_class.new
  end

  before(:each) do
    @mock_output = Limelight::Util::StringIO.new
    Limelight::Commands.output = @mock_output

    @mock_unpacker = make_mock("unpacker")
    Limelight::Util::Packer.stub!(:new).and_return(@mock_unpacker)
    @mock_unpacker.stub!(:unpack).and_return "unpacked location"

    Limelight::Main.stub!(:initialize_temp_directory)
  end

  it "should be listed" do
    Limelight::Commands::LISTING["unpack"].should == @command_class
  end

  it "set the context temp dir" do
    Limelight::Main.should_receive(:initialize_temp_directory)

    @command.run(["production_to_pack"])

  end

  it "should unpack a production" do
    @mock_unpacker.should_receive(:unpack).with("production_to_pack")

    @command.run(["production_to_pack"])
  end

  it "print the unpacked location" do
    @command.run(["production_to_pack"])

    @mock_output.should include("Production was unpacked to: unpacked location")
  end

end