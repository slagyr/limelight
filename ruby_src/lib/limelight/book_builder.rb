require 'limelight/book'

module Limelight
  
  def self.build_book(producer, &prop)
    builder = BookBuilder.new(producer)
    builder.instance_eval(&prop) if prop
    return builder.book
  end
  
  class BookBuilder
    
    attr_reader :book
    
    def initialize(producer)
      @book = Book.new(producer)
    end
    
    def default_page(page_name)
      @book.default_page = page_name
    end
    
  end
  
end