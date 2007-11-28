require 'limelight/limelight_java'
require 'limelight/block'

module Limelight
  class Page < Block
    
    include Java::limelight.Page
  
    attr_reader :styles
    attr_accessor :book
    getters :book
    setters :book
    
    def initialize
      super
      @page = self
      @styles = {}
    end
  
  end
end

# package limelight;
# 
# import java.util.Hashtable;
# import java.awt.*;
# 
# public class Page extends Block
# {
#   private Hashtable<String, Style> styles;
#   private Book book;
# 
#   public Page()
#   {
#     super();
#     setPage(this);
#     styles = new Hashtable<String, Style>();
#   }
# 
#   public Hashtable<String, Style> getStyles()
#   {
#     return styles;
#   }
# 
#   public void setBook(Book book)
#   {
#     this.book = book;
#   }
# 
#   public Book getBook()
#   {
#     return book;
#   }
# 
# 
#   
# }