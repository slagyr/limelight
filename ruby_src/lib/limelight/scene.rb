require 'limelight/java_util'
require 'limelight/prop'
require 'limelight/button_group_cache'

module Limelight
  class Scene < Prop
    
    include Java::limelight.ui.Scene
  
    attr_reader :button_groups, :styles, :casting_director
    attr_accessor :stage, :loader, :visible, :path
    getters :stage, :loader
    setters :stage
    
    def initialize(options={})
      super(options)
      @scene = self
      @button_groups = ButtonGroupCache.new
      illuminate
    end
  
    def add_options(options)
      @options = options
      illuminate
    end
    
    def illuminate
      @styles = @options.has_key?(:styles) ? @options.delete(:styles) : (@styles || {})
      @casting_director = @options.delete(:casting_director) if @options.has_key?(:casting_director)
      super
    end
  
  end
end