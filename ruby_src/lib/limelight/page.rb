require 'limelight/java_util'
require 'limelight/block'
require 'limelight/button_group_cache'

module Limelight
  class Page < Block
    
    include Java::limelight.ui.Page
  
    attr_reader :button_groups, :styles, :illuminator
    attr_accessor :book, :loader, :visible
    getters :book, :loader
    setters :book
    
    def initialize(options={})
      super(options)
      @page = self
      @button_groups = ButtonGroupCache.new
      illuminate
    end
  
    def add_options(options)
      @options = options
      illuminate
    end
    
    def illuminate
      @styles = @options.has_key?(:styles) ? @options.delete(:styles) : (@styles || {})
      @illuminator = @options.delete(:illuminator) if @options.has_key?(:illuminator)
      super
    end
  
  end
end