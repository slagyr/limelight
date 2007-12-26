require "rexml/document"
require 'limelight/page'
require 'limelight/block'
require 'limelight/styles'
require 'limelight/controllers/controllers'

module Limelight
  class LlmParser
  
    def initialize()
    end
  
    def parse(xml, loader)
      doc = REXML::Document.new(xml)
      @page = Page.new()
      @page.loader = loader
      populate(@page, doc.root)
      process_children(doc.root, @page)
      handle_styles(doc.root)   
      return @page
    end
  
    def process(element, parent)
      block = block_from(element.name)
      parent.add(block)
      populate(block, element)
      process_children(element, block)
      return block
    end
  
    def block_from(name)
      block = nil
      if(name == "page")
        block = Page.new()
      else
        block = Block.new()
      end
    
      return block
    end
  
    def populate(block, element)
      block.class_name = element.name 
      text = element.text ? element.text.strip : ""
      block.text = text
      add_extensions(element, block)
      element.attributes.each do |name, value|
        setter_sym = "#{name.downcase}=".to_sym
        if block.style.respond_to?(setter_sym)
          block.style.send(setter_sym, value)
        elsif name != "styles" && block.respond_to?(setter_sym)
          block.send(setter_sym, value)
        elsif block.respond_to?(name.to_sym)
          block.instance_eval("def #{name}(e); #{value}; end;") if name != "styles" && block.respond_to?(name.to_sym)
        end
      end
    end
  
    def process_children(element, block)
      element.children.each do |child|
        if child.is_a? REXML::Element
          process(child, block)
        end
      end
    end
  
    def handle_styles(element)
      styles_attr = element.attribute("styles")
      if styles_attr
        file = styles_attr.value
        Styles.load_into_page(file, @page)
        @page.load_style()
      end
    end
    
    def add_extensions(element, block)
      name = element.name
      add_extension(name, block) if name
      controllers_attr = element.attribute("controllers")
      if controllers_attr
        controllers = controllers_attr.value.split(" ")
        controllers.each { |controller_name| add_extension(controller_name, block) }
      end
    end
  
    def add_extension(name, block)  
      try_to_require(@page.loader.path_to("controllers/#{name}"))
      try_to_require("limelight/controllers/#{name}")
      module_name = name[0..0].upcase + name[1..-1]
      if Object.const_defined?(module_name)
        mod = Object.const_get(module_name)
        block.add_controller(mod)      
      elsif Limelight::Controllers.const_defined?(module_name)   
        mod = Limelight::Controllers.const_get(module_name)
        block.add_controller(mod)
      end
    end
    
    def try_to_require(name) 
      begin
        require name
      rescue Exception
        # ignore
      end
    end
  
  end
end