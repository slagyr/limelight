#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/studio'

describe Limelight::Studio do

  before do
    @studio = Limelight::Studio.instance
    @studio.productions.clear
  end

  it "should install itsself" do
    Limelight::Studio.install

    Limelight::Context.instance.studio.should == @studio
  end

  def prepare_for_open()
    @production = Limelight::Production.new("blah")
    @producer = mock("producer", :production => @production, :open => nil)
    Limelight::Producer.stub!(:new).and_return(@producer)
    return
  end

  it "should keep track of productions opened" do
    prepare_for_open()

    @studio.productions.length.should == 0

    @studio.open("blah")

    @studio.productions.length.should == 1
    @studio.productions[0].should be(@production)
    @production.studio.should == @studio
  end

  it "should ask all the production if it's okay to close and not shutdown" do
    prepare_for_open()
    @studio.open("blah")

    @production.should_receive(:allow_close?).and_return(false)
    @studio.should_allow_shutdown.should == false
  end

  it "should ask all the production if it's okay to close and shutdown" do
    prepare_for_open()
    @studio.open("blah")

    @production.should_receive(:allow_close?).and_return(true)
    @studio.should_allow_shutdown.should == true
  end

  it "should removed productions that are closed" do
    production1 = mock("production1")
    production2 = mock("production2")
    @studio.productions << production1 << production2

    @studio.production_closed(production1)

    @studio.productions.length.should == 1
    @studio.productions[0].should be(production2)
  end

  it "should shutdown if all the productions are closed" do
    prepare_for_open
    @studio.open("blah")
    Limelight::Context.instance().should_receive(:shutdown)

    @studio.production_closed(@production)

    Thread.pass
  end

end