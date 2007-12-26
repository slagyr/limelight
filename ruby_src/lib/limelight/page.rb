require 'limelight/java_util'
require 'limelight/block'

module Limelight
  class Page < Block
    
    include Java::limelight.Page
  
    attr_reader :styles
    attr_accessor :book, :loader
    getters :book, :loader
    setters :book
    
    def initialize
      super
      @page = self
      @styles = {}
    end
  
  end
end