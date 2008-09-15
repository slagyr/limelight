#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.
require 'limelight/util'

module Limelight
  
  def self.build_styles(style_hash = nil, &block)
    builder = StylesBuilder.new(style_hash)
    builder.instance_eval(&block) if block
    return builder.__styles__
  end
  
  class StylesBuilder

    Limelight::Util.lobotomize(self)
    
    attr_reader :__styles__
    
    def initialize(style_hash = nil)
      @__styles__ = style_hash || {}
    end
    
    def method_missing(sym, &block)
      __add_style__(sym.to_s, &block)
    end
    
    def __add_style__(name, &block)
      builder = StyleBuilder.new(name, self)
      builder.instance_eval(&block) if block
      @__styles__[name] = builder.__style__
    end
  end
  
  class StyleBuilder

    Limelight::Util.lobotomize(self)

    attr_reader :__style__
    
    def initialize(name, styles_builder, options = {})
      @__name = name
      @__styles_builder = styles_builder
      @__style__ = @__styles_builder.__styles__[name] || Styles::RichStyle.new
    end
    
    def hover(&block)
      @__styles_builder.__add_style__("#{@__name}.hover", &block)
    end

    def extends(*style_names)
      style_names.each do |style_name|
        extension = @__styles_builder.__styles__[style_name.to_s]
        raise StyleBuilderException.new("Can't extend missing style: '#{style_name}'") if extension.nil?
        @__style__.add_extension(extension)
      end
    end
                                                                                  
    def method_missing(sym, value)
      setter_sym = "#{sym}=".to_s
      raise StyleBuilderException.new("'#{sym}' is not a valid style") if !@__style__.respond_to?(setter_sym)
      @__style__.send(setter_sym, value.to_s)
    end
  end
  
  class StyleBuilderException < Exception
  end
  
end