require File.expand_path(File.dirname(__FILE__) + "/../spec_helper")
require 'limelight/loaders/file_page_loader'

describe Limelight::Loaders::FilePageLoader do

  before(:each) do
    @loader = Limelight::Loaders::FilePageLoader.new("/Users/micahmartin/Projects/limelight/example/sandbox.llm")
  end
  
  it "should load path parts" do
    @loader.root.should == ""
    @loader.page_file.should == "/Users/micahmartin/Projects/limelight/example/sandbox.llm"
    @loader.current_dir.should == "/Users/micahmartin/Projects/limelight/example"
  end
  
  it "should calculate relative file paths" do
    @loader.path_to(".").should == "/Users/micahmartin/Projects/limelight/example"
    @loader.path_to("..").should == "/Users/micahmartin/Projects/limelight"
    @loader.path_to("images/blah.gif").should == "/Users/micahmartin/Projects/limelight/example/images/blah.gif"
  end
  
  it "should know absolute paths" do
    @loader.path_to("/").should == "/"
    @loader.path_to("/Users").should == "/Users"
  end
  
  # it "should deal with windows paths" do
  #   @loader = Limelight::Loaders::FileLoader.new("C:\\Projects\\limelight\\example\\sandbox.llm")
  #   @loader.root.should == "C:/"
  #   @loader.page_file.should == "C:\\Projects\\limelight\\example\\sandbox.llm"
  #   @loader.current_dir.should == "C:/Projects/limelight/example/"
  # end

end
