#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../spec_helper")
require 'limelight/commands/help_command'

describe Limelight::Commands::HelpCommand do

  before(:all) do
    @command_class = Limelight::Commands::HelpCommand
    @command = @command_class.new
    Limelight::Commands["help"] #load listing
  end

  before(:each) do
    @mock_output = StringIO.new
    Limelight::Commands.output = @mock_output
  end

  it "should be listed" do
    Limelight::Commands["help"].should == @command_class
  end

  it "should print all commands not starting in --" do
    @command.run([])

    Limelight::Commands::LISTING.keys.each do |key|
      if key[0...1] == "-"
        @mock_output.string.include?("\t#{key}").should == false
      else
        @mock_output.string.include?("\t#{key}").should == true
      end
    end
  end

  it "should include options in the header" do
    @command.run([])

    @mock_output.string.include?("[--version]").should == true
  end

end