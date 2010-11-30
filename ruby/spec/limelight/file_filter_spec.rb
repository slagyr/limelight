#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/file_filter'

describe Limelight::FileFilter do

  before(:each) do
  end
  
  it "should have a description and a block" do
    filter = Limelight::FileFilter.new("Some Description") { "blah" }
    
    filter.description.should == "Some Description"
    filter.filter.call.should == "blah"
  end
  
  it "should have implement java interface" do
    filter = Limelight::FileFilter.new("Some Description") { |file| file.name == "good" }
    
    filter.getDescription().should == "Some Description"
    bad_file = mock("file", :name => "bad")
    good_file = mock("file", :name => "good")
    filter.accept(bad_file).should == false
    filter.accept(good_file).should == true
  end

end
