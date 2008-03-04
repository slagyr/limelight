require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/book_builder'
require 'limelight/book'

describe Limelight::BookBuilder do

  before(:each) do
    @producer = make_mock("producer")
  end
  
  it "should build a book" do
    book = Limelight.build_book(@producer) 
    
    book.class.should == Limelight::Book
  end

  it "should allow default scene" do
    book = Limelight.build_book(@producer) do
      default_scene :front_scene
    end
    
    book.default_scene.should == :front_scene
  end
end
