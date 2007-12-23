require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/book'

describe Limelight::Book do

  before(:each) do
    @path = "/Users/micahmartin/Projects/limelight/text.llm"
    @book = Limelight::Book.new
    @loader = mock("loader")
    Limelight::Loaders::FileLoader.should_receive(:new).with(@path).and_return(@loader)
    @parser = mock("parser")
    Limelight::LlmParser.should_receive(:new).and_return(@parser)
  end

# This too ugly to be right.  Book's design probably needs to change.
  
  # it "should load using a loader" do
  #   @loader.should_receive(:page_file).and_return(@path)
  #   @loader.should_receive(:load).with(@path).and_return("some content")
  #   @loader.should_receive(:current_dir).and_return("test dir")
  #   @parser.should_receive(:parse).with("some content", @loader).and_return("a page object")
  #   @book.should_receive(:open).with("a page object")
  #   
  #   @book.load(@path)
  #   
  #   @book.loader.should == @loader
  #   $:.should include("test dir")
  # end
  # 
  # it "should set the current page on loading" do
  #   @loader.stubs!(:page_file => @path, :current_dir => "dir", :load => "some content")
  #   page = make_mock("page")
  #   @parser.stubs!(:parse => page)
  #   
  #   page.should_receive(:loader=).with(@loader)
  #   page.should_receive(:book=).with(@book)
  #   @book.should_receive(:open).with(page)
  #   
  #   @book.load(@path)
  # end

end