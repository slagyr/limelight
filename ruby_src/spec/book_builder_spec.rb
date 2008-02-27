require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/book_builder'
require 'limelight/book'

describe Limelight::BookBuilder do

  before(:each) do
  end
  
  it "should build a book" do
    book = Limelight.build_book 
    
    book.class.should == Limelight::Book
  end

  it "should allow default page" do
    book = Limelight.build_book do
      default_page :front_page
    end
    
    book.default_page.should == :front_page
  end
end
