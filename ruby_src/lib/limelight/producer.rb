require 'limelight/loaders/file_page_loader'
require 'limelight/block_builder'
require 'limelight/styles_builder'
require 'limelight/illuminator'
require 'limelight/book'
require 'limelight/build_exception'
require 'limelight/book_builder'

module Limelight

  class Producer

    def self.open(page_name)
      producer = new(page_name)
      producer.open
    end
    
    attr_reader :loader
    
    def initialize(root_path)
      @loader = Loaders::FilePageLoader.for_root(root_path)
    end
    
    def open()
      if @loader.exists?("index.rb")
        @book = open_book
        open_page(@book.default_page)
      else
        @book = Book.new(self)
        open_page(".")
      end
    end
    
    def open_book
      content = @loader.load("index.rb")
      book = Limelight.build_book(self) do
        begin
          eval content
        rescue Exception => e
          raise BuildException.new("index.rb", content, e)
        end
      end
      
      book.styles = load_styles('.')
      
      return book
    end
    
    def open_page(path)
      page_specific_loader = Loaders::FilePageLoader.for_root(loader.path_to(path)) #TODO - MDM - Shouldn't really be needed
      styles = load_styles(path)
      merge_with_book_styles(styles)
      illuminator = Illuminator.new(page_specific_loader)
      
      page = load_blocks(path, :styles => styles, :illuminator => illuminator, :loader => @loader)
      
      @book.open(page)
    end
    
    def load_blocks(path, options = {})
      filename = File.join(path, "blocks.rb")
      content = @loader.load(filename)
      options[:build_loader] = @loader
      return Limelight.build_page(options) do
        begin
          eval content
        rescue Exception => e
          raise BuildException.new(filename, content, e)
        end
      end
    end
    
    def load_styles(path)
      filename = File.join(path, "styles.rb")
      content = @loader.load(filename)
      return  Limelight.build_styles do
        begin
          eval content
        rescue Exception => e
          raise BuildException.new(filename, content, e)
        end
      end
    end
    
    def merge_with_book_styles(styles)
      @book.styles.each_pair do |key, value|
        styles[key] = value if !styles.has_key?(key)
      end
    end

  end
  
end