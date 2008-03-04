require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/book'
require 'limelight/loaders/file_scene_loader'

describe Limelight::Book do

  before(:each) do
    @producer = make_mock("producer")
    @book = Limelight::Book.new(@producer)
  end
  
  it "should have a producer" do
    @book.producer.should == @producer
  end
  
  it "should init defaults" do
    @book.styles.class.should == Hash
    @book.styles.size.should == 0
  end

end