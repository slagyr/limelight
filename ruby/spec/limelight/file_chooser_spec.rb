#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/file_chooser'

describe Limelight::FileChooser do

  before(:each) do
  end
  
  it "should create a Java file chooser" do
    chooser = Limelight::FileChooser.new()
    chooser.chooser.class.should == javax.swing.JFileChooser
  end
  
  it "should set current directory if provided" do
    chooser = Limelight::FileChooser.new(:directory => "/tmp")
    
    chooser.chooser.getCurrentDirectory().getAbsolutePath().should == "/tmp"
  end
  
  it "should have a title if provided" do
    chooser = Limelight::FileChooser.new(:title => "Some Title")
    
    chooser.chooser.getDialogTitle().should == "Some Title"
  end
  
  it "should have a filter if provided" do
    chooser = Limelight::FileChooser.new(:description => "Description") { "filter" }
    
    filter = chooser.chooser.getFileFilter()
    filter.description.should == "Description"
    filter.filter.call.should == "filter"
  end
  
  it "should return nil if no file was selected" do
    chooser = Limelight::FileChooser.new(:parent => "parent")
    chooser.chooser.should_receive(:showOpenDialog).with("parent").and_return(javax.swing.JFileChooser::CANCEL_OPTION)
    
    chooser.choose_file.should == nil
  end
  
  it "should return the absolute path if a file was selected" do    
    chooser = Limelight::FileChooser.new(:parent => "parent")
    chooser.chooser.should_receive(:showOpenDialog).with("parent").and_return(javax.swing.JFileChooser::APPROVE_OPTION)
    selected_file = mock("selected file", :absolute_path => "selected file's absolute path")
    chooser.chooser.should_receive(:getSelectedFile).and_return(selected_file)

    chooser.choose_file.should == "selected file's absolute path"
  end
  
  it "should default to files and directories but allow directory only and file only" do
    chooser = Limelight::FileChooser.new
    chooser.chooser.file_selection_mode.should == javax.swing.JFileChooser::FILES_AND_DIRECTORIES
        
    chooser = Limelight::FileChooser.new(:directories_only => true)
    chooser.chooser.file_selection_mode.should == javax.swing.JFileChooser::DIRECTORIES_ONLY
    
    chooser = Limelight::FileChooser.new(:files_only => true)
    chooser.chooser.file_selection_mode.should == javax.swing.JFileChooser::FILES_ONLY
  end

end
