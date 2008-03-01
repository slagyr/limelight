require 'limelight/book'

module Limelight
  
  def self.build_book(producer, &block)
    builder = BookBuilder.new(producer)
    builder.instance_eval(&block) if block
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