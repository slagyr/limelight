require 'limelight/scene'
require 'limelight/prop'
require 'limelight/build_exception'

module Limelight
  
  def self.build_scene(options={}, &prop)
    loader = options.delete(:build_loader)
    builder = SceneBuilder.new(options)
    builder.__loader__ = loader
    builder.instance_eval(&prop) if prop
    return builder.__prop__
  end
  
  class PropBuilder
    attr_reader :__prop__
    attr_accessor :__loader__
    
    def initialize(options)
      @__prop__ = Prop.new(options)
    end
    
    def __(options)
      @__prop__.add_options(options)
    end
    
    def __install(file)
      raise "Cannot install external props because no loader was provided" if @__loader__.nil?
      raise "External prop file: '#{file}' doesn't exist" if !@__loader__.exists?(file)
      content = @__loader__.load(file)
      begin
        self.instance_eval(content)
      rescue Exception => e
        raise BuildException.new(file, content, e)
      end
    end
    
    def method_missing(sym, options={}, &prop)
      options[:class_name] ||= sym.to_s
      builder = PropBuilder.new(options)
      builder.__loader__ = @__loader__
      builder.instance_eval(&prop) if prop
      @__prop__.add(builder.__prop__)
    end
  end
  
  class SceneBuilder < PropBuilder
    def initialize(options)
      @__prop__ = Scene.new(options)
    end
  end
  
end