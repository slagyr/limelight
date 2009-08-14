#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/studio'

Studio = Limelight::Studio

describe Limelight::Studio do

  class TestProduction
    attr_accessor :name
  end

  before do
    Studio.reset
    @production = TestProduction.new
  end

  it "should install itsself" do
    Studio.install

    Limelight::Context.instance.studio.should == Studio.instance
  end

  def prepare_for_open()
    @producer = mock("producer", :production => @production, :open => nil)
    Limelight::Producer.stub!(:new).and_return(@producer)
  end

  it "should keep track of productions opened" do
    prepare_for_open
    @production.name = "Sven"
    Studio.productions.length.should == 0

    Studio.open(@production)

    Studio.productions.length.should == 1
    Studio["Sven"].should be(@production)
  end

  it "should ask all the production if it's okay to close and not shutdown" do
    Studio.index(@production)

    @production.should_receive(:allow_close?).and_return(false)
    Studio.should_allow_shutdown.should == false
  end

  it "should ask all the production if it's okay to close and shutdown" do
    Studio.index(@production)

    @production.should_receive(:allow_close?).and_return(true)
    Studio.should_allow_shutdown.should == true
  end

  it "should removed productions that are closed" do
    production1 = TestProduction.new
    production2 = TestProduction.new
    Studio.index(production1)
    Studio.index(production2)

    Studio.production_closed(production1)

    Studio.productions.length.should == 1
    Studio.productions[0].should be(production2)
  end

  it "should shutdown if all the productions are closed" do
    prepare_for_open
    Studio.open("blah")
    Limelight::Context.instance().should_receive(:shutdown)

    Studio.production_closed(@production)

    sleep(0.1)
  end

  it "should add productions to the index" do
    @production.name = "Bob"

    Limelight::Studio.index(@production)

    Limelight::Studio["Bob"].should == @production
  end

  it "should give a production a name if it doesn't have one" do
    Limelight::Studio.index(@production)

    @production.name.should == "1"
    Limelight::Studio["1"].should == @production

    production2 = TestProduction.new
    Limelight::Studio.index(production2)

    production2.name.should == "2"
    Limelight::Studio["2"].should == production2
  end

  it "should modify the name of a production until it's unique" do
    prod1 = TestProduction.new
    prod2 = TestProduction.new
    prod3 = TestProduction.new
    prod1.name = prod2.name = prod3.name = "Fido"

    Studio.index(prod1)
    Studio.index(prod2)
    Studio.index(prod3)

    Studio["Fido"].should be(prod1)
    Studio["Fido_2"].should be(prod2)
    Studio["Fido_3"].should be(prod3)
    prod1.name.should == "Fido"
    prod2.name.should == "Fido_2"
    prod3.name.should == "Fido_3"
  end

  it "should shutdown" do
    Studio.index(@production)
    @production.should_receive(:allow_close?).and_return(true)
    @production.should_receive(:close).and_return(true)
    Limelight::Context.instance().should_receive(:shutdown)

    Studio.shutdown
    sleep(0.1)
  end

  it "should publish on drb" do
    Studio.publish_on_drb("9876")

    DRb.start_service
    proxy = DRbObject.new(nil, "druby://127.0.0.1:9876")
    proxy.instance.should_not == nil
  end

  it "should provide the utilities production" do
    Studio.utilities_production.should_not == nil
    Studio.utilities_production.name.should == "utilities_production"
    Studio.utilities_production.should be(Studio.utilities_production)
#    Studio.utilities_production.close
  end

end