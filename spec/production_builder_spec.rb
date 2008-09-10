#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/production_builder'

describe Limelight::ProductionBuilder do

  before(:each) do
    @producer = make_mock("producer")
    @theater = make_mock("theater")
  end
  
  it "should build a production" do
    result = Limelight.build_production(@producer, @theater)
    
    result.class.should == Limelight::Production
    result.producer.should == @producer
    result.theater.should == @theater
  end
  
  it "should build able to set the production's name" do
    result = Limelight.build_production(@producer, @theater) do
      name "My Production"
    end
    
    result.name.should == "My Production"
  end
  
  it "should build attribute accessors" do
    result = Limelight.build_production(@producer, @theater) do
      name "My Production2"
      attribute :foo
    end  
    
    result.respond_to?(:foo).should == true
    result.foo = "foo"
    result.foo.should == "foo"
  end
  
  it "should raise an exception when setting an invalid property" do
    lambda do
      Limelight::build_production(@producer, @theater) do
        blah "blah"
      end
    end.should raise_error(Limelight::ProductionBuilderException, "'blah' is not a valid production property")
  end
end