require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/commands/help_command'
require 'limelight/util/string_io'

describe Limelight::Commands::HelpCommand do

  before(:all) do
    @command_class = Limelight::Commands::HelpCommand
    @command = @command_class.new
    Limelight::Commands["help"] #load listing
  end

  before(:each) do
    @mock_output = Limelight::Util::StringIO.new
    Limelight::Commands.output = @mock_output
  end

  it "should be listed" do
    Limelight::Commands["help"].should == @command_class
  end

  it "should print all commands not starting in --" do
    @command.run([])

    Limelight::Commands::LISTING.keys.each do |key|
      if key[0...1] == "-"
        @mock_output.should_not include("\t#{key}")
      else
        @mock_output.should include("\t#{key}")
      end
    end
  end

  it "should include options in the header" do
    @command.run([])

    @mock_output.should include("[--version]")
  end

end