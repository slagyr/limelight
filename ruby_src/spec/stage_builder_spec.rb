require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/stage_builder'
require 'limelight/theater'

describe Limelight::StageBuilder do
  
  before(:each) do
    @theater = Limelight::Theater.new
    @producer = make_mock("producer", :theater => @theater)
  end

  it "should give no stages if empty" do
    stages = Limelight::build_stages(@producer)
    
    stages.should == []
  end
  
  it "should build one stage" do
    stages = Limelight::build_stages(@producer) do
      stage "George"
    end
    
    stages.length.should == 1
    stages[0].name.should == "George"
  end
  
  it "should build multiple stages" do
    stages = Limelight::build_stages(@producer) do
      stage "George"
      stage "Bill"
      stage "Amy"
    end
    
    stages.length.should == 3
    stages[0].name.should == "George"
    stages[1].name.should == "Bill"
    stages[2].name.should == "Amy"
  end
  
  it "should allow the setting of stage properties" do
    stages = Limelight::build_stages(@producer) do
      stage "George" do
        title "The Curious"
      end
    end
    
    stages.length.should == 1
    stages[0].title.should == "The Curious"
  end
  
  it "should raise an exception when setting an invalid property" do
    lambda do
        Limelight::build_stages(@producer) do
        stage "George" do
          blah "blah"
        end
      end
    end.should raise_error(Limelight::StageBuilderException, "'blah' is not a valid stage property")
  end

  it "should add stages to the theater" do
    stages = Limelight::build_stages(@producer) do
      stage "George" do
        title "The Curious"
      end
    end
    
    @theater["George"].should == stages[0]
  end

  it "should add stages to the theater" do
    stages = Limelight::build_stages(@producer) do
      stage "George" do
        title "The Curious"
      end
    end
    stages = Limelight::build_stages(@producer) do
      stage "George" do
        title "The Monkey"
      end
    end
    
    @theater.stages.length.should == 1
    @theater["George"].title.should == "The Monkey"
  end
  
end

