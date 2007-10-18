require "rexml/document"

class LlmParser
  
  def initialize()
  end
  
  def parse(xml)
    doc = REXML::Document.new(xml)
    page = Page.new()
    populate(page, doc.root)
    process_children(doc.root, page, page)
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
    if(name == "page")
      return Page.new()
    else
      return Block.new()
    end
  end
  
  def populate(block, element)
    block.name = element.name
    text = element.text ? element.text.strip : ""
    block.text = text if text.size > 0
  end
  
  def process_children(element, block, page)
    element.children.each do |child|
      if child.is_a? REXML::Element
        process(child, block, page)
      end
    end
  end
  
end