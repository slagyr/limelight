#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  
  def self.build_styles(&block)
    builder = StylesBuilder.new
    builder.instance_eval(&block) if block
    return builder.__styles
  end
  
  class StylesBuilder
    attr_reader :__styles
    
    def initialize
      @__styles = {}
    end
    
    def method_missing(sym, &block)
      __add_style(sym.to_s, &block)
    end
    
    def __add_style(name, &block)
      builder = StyleBuilder.new(name, self)
      builder.instance_eval(&block) if block
      @__styles[name] = builder.__style
    end
  end
  
  class StyleBuilder
    attr_reader :__style
    
    def initialize(name, styles_builder)
      @__style = UI::RichStyle.new
      @__name = name
      @__styles_builder = styles_builder
    end
    
    def hover(&block)
      @__styles_builder.__add_style("#{@__name}.hover", &block)
    end

    def extends(*style_names)
      style_names.each do |style_name|
        extension = @__styles_builder.__styles[style_name.to_s]
        raise StyleBuilderException.new("Can't extend missing style: '#{style_name}'") if extension.nil?
        @__style.add_extension(extension)
      end
    end
                                                                                  
    def method_missing(sym, value)
      setter_sym = "#{sym}=".to_s
      raise StyleBuilderException.new("'#{sym}' is not a valid style") if !@__style.respond_to?(setter_sym)
      @__style.send(setter_sym, value.to_s)
    end
  end
  
  class StyleBuilderException < Exception
  end
  
end