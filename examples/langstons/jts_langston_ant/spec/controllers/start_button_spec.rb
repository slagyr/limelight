require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'langston_ant/controllers/start_button'

class MockStartButton
  def initialize
    extend StartButton
  end
end

describe StartButton do

  before(:each) do
    @button = MockStartButton.new
  end
  
  it "should find the plane and start it up" do
    mock_plane = make_mock("plane")
    mock_page = make_mock("page")
    @button.should_receive(:page).and_return(mock_page)
    mock_page.should_receive(:find).with("plane").and_return(mock_plane)
    mock_plane.should_receive(:run)
    
    @button.button_pressed(nil)
  end
  

end

