
module Limelight
  
  def self.build_styles(&block)
    builder = StylesBuilder.new
    builder.instance_eval(&block) if block
    return builder.__styles__
  end
  
  class StylesBuilder
    attr_reader :__styles__
    
    def initialize
      @__styles__ = {}
    end
    
    def method_missing(sym, &block)
      __add_style(sym.to_s, &block)
    end
    
    def __add_style(name, &block)
      builder = StyleBuilder.new(name, self)
      builder.instance_eval(&block) if block
      @__styles__[name] = builder.__style__
    end
  end
  
  class StyleBuilder
    attr_reader :__style__
    
    def initialize(name, styles_builder)
      @__style__ = Java::limelight.ui.FlatStyle.new  
      @__name__ = name
      @__styles_builder = styles_builder
    end
    
    def hover(&block)
      @__styles_builder.__add_style("#{@__name__}.hover", &block)
    end
    
    def method_missing(sym, value)
      setter_sym = "#{sym}=".to_s
      raise StyleBuilderException.new(sym) if !@__style__.respond_to?(setter_sym)
      @__style__.send(setter_sym, value.to_s)
    end
  end
  
  class StyleBuilderException < Exception
    def initialize(name)
      super("'#{name}' is not a valid style")
    end
  end
  
end