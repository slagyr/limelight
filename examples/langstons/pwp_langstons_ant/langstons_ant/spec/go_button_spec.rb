require File.expand_path(File.dirname(__FILE__) + "/../controllers/go_button")

describe "Go Button" do
  
  class TestGoButton
    include GoButton
  end               
  
  before(:each) do
    @button = TestGoButton.new
  end
                             
  it "should move north, black" do
    id, direction = @button.next_id("50", "50", :north, "black")
    id.should == "51_50"
    direction.should == :east
  end
  
  it "should move north, white" do
    id, direction = @button.next_id("50", "50", :north, "white")
    id.should == "49_50"
    direction.should == :west
  end                 
  
  it "should move east, black" do
    id, direction = @button.next_id("50", "50", :east, "black")
    id.should == "50_49"
    direction.should == :south
  end
  
  it "should move east, white" do
    id, direction = @button.next_id("50", "50", :east, "white")
    id.should == "50_51"
    direction.should == :north
  end                                                        
  
  it "should move west, black" do
    id, direction = @button.next_id("50", "50", :west, "black")
    id.should == "50_51"
    direction.should == :north
  end                         
  
  it "should move west, white" do
    id, direction = @button.next_id("50", "50", :west, "white")
    id.should == "50_49"
    direction.should == :south
  end
  
  it "should move south, black" do
    id, direction = @button.next_id("50", "50", :south, "black")
    id.should == "49_50"
    direction.should == :west 
  end                        
  
  it "should move south, white" do
    id, direction = @button.next_id("50", "50", :south, "white")
    id.should == "51_50"
    direction.should == :east
  end  
  
  it "should reverse color" do
    @button.reverse_color("white").should == "black"
    @button.reverse_color("black").should == "white"
  end
end