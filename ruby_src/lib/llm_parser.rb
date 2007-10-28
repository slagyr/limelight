require "rexml/document"
require 'page'
require 'block'
require 'styles'

class LlmParser
  
  def initialize()
  end
  
  def parse(xml)
    doc = REXML::Document.new(xml)
    page = Page.new()
    populate(page, doc.root)
    process_children(doc.root, page, page)
    handle_styles(page, doc.root)
    return page
  end
  
  def process(element, parent, page)
    block = block_from(element.name)
    parent.add(block)
    populate(block, element)
    process_children(element, block, page)
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
    block.name = element.name
    add_extension(block.name, block)
    text = element.text ? element.text.strip : ""
    block.text = text if text.size > 0
    element.attributes.each do |name, value|
      setter_sym = "#{name.downcase}=".to_sym
      block.send(setter_sym, value) if block.respond_to?(setter_sym)
      block.style.send(setter_sym, value) if block.style.respond_to?(setter_sym)
    end
  end
  
  def process_children(element, block, page)
    element.children.each do |child|
      if child.is_a? REXML::Element
        process(child, block, page)
      end
    end
  end
  
  def handle_styles(page, element)
    styles_attr = element.attribute("styles")
    if styles_attr
      file = styles_attr.value
      Styles.load_into_page(file, page)
      page.loadStyle()
    end
  end
  
  def add_extension(name, block)
    if File.exists?("#{name}.rb")
      load "#{name}.rb"
      module_name = name[0..0].upcase + name[1..-1]
      mod = eval(module_name)
      block.instance_eval { extend mod }
      block.extended if block.respond_to?(:extended)
    end
  end
  
end