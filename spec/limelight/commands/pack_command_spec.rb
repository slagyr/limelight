#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../spec_helper")
require 'limelight/commands/pack_command'

describe Limelight::Commands::PackCommand do

  before(:all) do
    @command_class = Limelight::Commands::PackCommand
    @command = @command_class.new
  end

  it "should be listed" do
    Limelight::Commands::LISTING["pack"].should == @command_class
  end

  it "should pack a production" do
    mock_packer = mock("packer")
    Limelight::Util::Packer.should_receive(:new).and_return(mock_packer)
    mock_packer.should_receive(:pack).with("production_to_pack")

    @command.run(["production_to_pack"])
  end

end