def bigger!
  styled_texts.each do |styled_text|
    font_size = styled_text.style.font_size
    new_font_size = font_size.to_i + 1
    styled_text.style.font_size = new_font_size
  end
end

def smaller!
  styled_texts.each do |styled_text|
    font_size = styled_text.style.font_size
    new_font_size = font_size.to_i - 1
    styled_text.style.font_size = new_font_size
  end
end

def lighter!
  styled_texts.each do |styled_text|
    color = styled_text.style.text_color
    color_obj = Java::limelight.util.Colors.resolve(color)
    new_color_obj = color_obj.brighter
    new_color = Java::limelight.util.Colors.toString(new_color_obj)
    styled_text.style.text_color = new_color
  end
end

def darker!
  styled_texts.each do |styled_text|
    color = styled_text.style.text_color
    color_obj = Java::limelight.util.Colors.resolve(color)
    new_color_obj = color_obj.darker
    new_color = Java::limelight.util.Colors.toString(new_color_obj)
    styled_text.style.text_color = new_color
  end
end

private #################################################

def text_section_parent
  prop = parent
  prop = prop.parent while prop.name != "text_section"
  return prop
end

def styled_texts
  return text_section_parent.find_by_name("styled_text")
end
