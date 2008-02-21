require 'limelight/page'
require 'limelight/block'

module Limelight
  
  def self.build_page(options={}, &block)
    builder = PageBuilder.new(options)
    builder.instance_eval(&block) if block
    return builder.__block__
  end
  
  class BlockBuilder
    attr_reader :__block__
    
    def initialize(options)
      @__block__ = Block.new(options)
    end
    
    def method_missing(sym, options={}, &block)
      options[:class_name] ||= sym.to_s
      block_builder = BlockBuilder.new(options)
      block_builder.instance_eval(&block) if block
      @__block__.add(block_builder.__block__)
    end
  end
  
  class PageBuilder < BlockBuilder
    def initialize(options)
      @__block__ = Page.new(options)
    end
  end
  
end