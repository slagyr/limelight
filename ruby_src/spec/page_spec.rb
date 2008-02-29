require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/page'

describe Limelight::Page do

  before(:each) do
    @illuminator = make_mock("illuminator", :fill_cast => nil)
    @page = Limelight::Page.new(:illuminator => @illuminator)
  end
  
  it "should have a styles hash" do
    @page.styles.should_not == nil
    @page.styles.size.should == 0
  end

  it "should have a button group cache" do
    @page.button_groups.should_not == nil
    @page.button_groups.class.should == Java::limelight.ui.ButtonGroupCache
  end
  
  it "should pullout sytles and casting_director from options" do
    page = Limelight::Page.new(:styles => "styles", :illuminator => @illuminator)
    
    page.styles.should == "styles"
    page.illuminator.should == @illuminator
  end
end
