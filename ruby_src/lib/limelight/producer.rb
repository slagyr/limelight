require 'limelight/loaders/file_page_loader'
require 'limelight/block_builder'
require 'limelight/styles_builder'
require 'limelight/illuminator'
require 'limelight/book'

module Limelight

  class Producer

    def self.open(page_name)
      loader = Loaders::FilePageLoader.for_root(page_name)
      
      page = Limelight.build_page do
        eval loader.load("blocks.rb")
      end
      
      styles = Limelight.build_styles do
        eval loader.load("styles.rb")
      end
      
      page.styles = styles
      page.loader = loader
      
      page.stylize
      Illuminator.new(loader).illuminate(page)
      Book.new.open(page)
    end

  end
  
end