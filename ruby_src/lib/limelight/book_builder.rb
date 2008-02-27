require 'limelight/book'

module Limelight
  
  def self.build_book(&block)
    builder = BookBuilder.new
    builder.instance_eval(&block) if block
    return builder.book
  end
  
  class BookBuilder
    
    attr_reader :book
    
    def initialize
      @book = Book.new
    end
    
    def default_page(page_name)
      @book.default_page = page_name
    end
    
  end
  
end