require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/production'

describe Limelight::Production, "Instance methods" do

  before(:each) do
    @producer = make_mock("producer")
    @theater = make_mock("theater")
    @production = Limelight::Production.new(@producer, @theater)
  end
  
  it "should know it producder and theater" do
    @production.producer.should == @producer
    @production.theater.should == @theater
  end
  
  it "should be indexed" do
    Limelight::Production[@production.name].should == @production
  end
  
  it "should raise an error when setting the name to a duplicate name" do
    @production.name = "Bill"
    
    production = Limelight::Production.new(@producer, @theater)
    lambda { production.name = "Bill" }.should raise_error(Limelight::LimelightException, "Production name 'Bill' is already taken")
  end

end

describe Limelight::Production, "Class methods" do
  
  class TestProduction
    attr_accessor :name
  end
  
  before(:each) do
    Limelight::Production.clear_index
    @production = TestProduction.new
  end
  
  it "should add productions to the index" do
    @production.name = "Bob"
    
    Limelight::Production.index(@production)
    
    Limelight::Production["Bob"].should == @production
  end
  
  it "should give a production a name if it doesn't have one" do
    Limelight::Production.index(@production)
    
    @production.name.should == "1"
    Limelight::Production["1"].should == @production
    
    production2 = TestProduction.new
    Limelight::Production.index(production2)
    
    production2.name.should == "2"
    Limelight::Production["2"].should == production2
  end
  
  it "should raise an error if adding a duplicate name" do
    production2 = TestProduction.new
    
    @production.name = "Bob"
    production2.name = "Bob"
    
    Limelight::Production.index(@production)
    lambda { Limelight::Production.index(production2) }.should raise_error(Limelight::LimelightException, "Production name 'Bob' is already taken")
  end
  
end
