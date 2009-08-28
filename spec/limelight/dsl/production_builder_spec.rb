#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../../spec_helper")
require 'limelight/dsl/production_builder'

describe Limelight::DSL::ProductionBuilder do

  before(:each) do
    @production = Limelight::Production.new("/tmp")
  end
  
  it "should build a production" do
    result = Limelight.build_production(@production)
    
    result.should == @production
    result.path.should == "/tmp"
  end
  
  it "should build able to set the production's name" do
    result = Limelight.build_production(@production) do
      name "My Production"
    end
    
    result.name.should == "My Production"
  end
  
  it "should build attribute accessors" do
    result = Limelight.build_production(@production) do
      name "My Production2"
      attribute :foo
    end  
    
    result.respond_to?(:foo).should == true
    result.foo = "foo"
    result.foo.should == "foo"
  end
  
  it "should raise an exception when setting an invalid property" do
    lambda do
      Limelight::build_production(@production) do
        blah "blah"
      end
    end.should raise_error(Limelight::DSL::ProductionBuilderException, "'blah' is not a valid production property")
  end
end