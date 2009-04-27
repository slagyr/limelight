#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

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
    Limelight::Producer.should_receive(:open).with("production_name", :drb_port => nil )

    @command.run(["production_name"])
  end

  it "should open the default production" do
    Limelight::Main.should_receive(:initialize_context)
    Limelight::Producer.should_receive(:open).with(@command_class::DEFAULT_PRODUCTION, :drb_port => nil )

    @command.run([])
  end

  it "should parse drp_port option" do
    @command.parse ["production_path"]
    @command.drb_port.should == nil

    @command.parse ["-d", "1234", "some_prod"]
    @command.drb_port.should == "1234"

    @command.parse ["--drb_port=4321", "some_prod"]
    @command.drb_port.should == "4321"
  end

end