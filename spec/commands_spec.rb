#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/commands'
require 'limelight/producer'

describe Limelight::Commands do

  before(:each) do
  end

  it "should open a production" do
    args = ["open", "production_name"]
    Limelight::Producer.should_receive(:open).with("production_name")
    Limelight::Commands.run(args)
  end

  it "should open the default production" do
    args = ["open"]
    Limelight::Producer.should_receive(:open).with(Limelight::DEFAULT_PRODUCTION)
    Limelight::Commands.run(args)
  end

  it "should pack a production" do
    mock_packer = make_mock("packer")
    Limelight::Util::Packer.should_receive(:new).and_return(mock_packer)
    mock_packer.should_receive(:pack).with("production_to_pack")

    Limelight::Commands.run(["pack", "production_to_pack"])
  end

end