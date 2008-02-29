require 'limelight/page'
require 'limelight/block'
require 'limelight/build_exception'

module Limelight
  
  def self.build_page(options={}, &block)
    loader = options.delete(:build_loader)
    builder = PageBuilder.new(options)
    builder.__loader__ = loader
    builder.instance_eval(&block) if block
    return builder.__block__
  end
  
  class BlockBuilder
    attr_reader :__block__
    attr_accessor :__loader__
    
    def initialize(options)
      @__block__ = Block.new(options)
    end
    
    def __(options)
      @__block__.add_options(options)
    end
    
    def __install(file)
      raise "Cannot install external blocks because no loader was provided" if @__loader__.nil?
      raise "External block file: '#{file}' doesn't exist" if !@__loader__.exists?(file)
      content = @__loader__.load(file)
      begin
        self.instance_eval(content)
      rescue Exception => e
        raise BuildException.new(file, content, e)
      end
    end
    
    def method_missing(sym, options={}, &block)
      options[:class_name] ||= sym.to_s
      builder = BlockBuilder.new(options)
      builder.__loader__ = @__loader__
      builder.instance_eval(&block) if block
      @__block__.add(builder.__block__)
    end
  end
  
  class PageBuilder < BlockBuilder
    def initialize(options)
      @__block__ = Page.new(options)
    end
  end
  
end