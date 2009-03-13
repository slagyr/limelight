#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module PropTree
  
  def build_tree(scene)
    self.remove_all
    add_prop_row(scene, 0)
    update
  end
  
  def highlight(prop)
    children.each do |child|
      if child.prop == prop
        child.style.background_color = "#B8C6F2"
        child.update
      elsif(child.style.background_color != "white")
        child.style.background_color = "white"
        child.update
      end
    end
  end
  
  private #################################################
  
  def add_prop_row(prop, indent)
    add Limelight::Prop.new(:name => "prop_row", :left_padding => indent.to_s, :prop => prop, :text => "#{prop.name}:#{prop.id}")
    prop.children.each do |child|
      add_prop_row(child, indent + 10)
    end
  end
  
end