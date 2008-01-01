require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/page'

describe Limelight::Page do

  before(:each) do
    @page = Limelight::Page.new
  end
  
  it "should have a styles hash" do
    @page.styles.should_not == nil
    @page.styles.size.should == 0
  end

  it "should have a button group cache" do
    @page.button_groups.should_not == nil
    @page.button_groups.class.should == Java::limelight.ButtonGroupCache
  end
end
