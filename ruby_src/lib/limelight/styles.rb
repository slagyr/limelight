require 'yaml'

module Limelight
  
  class Styles

    def self.load(content)
      yamalized_content = content.gsub("\t", "  ").gsub("#", "pigpen")
      return YAML.load(yamalized_content)
    end

    def self.load_into_page(filename, page)
      content = page.loader.load(filename)
      style_defs = load(content)
      styles = page.styles;
  
      style_defs.each_pair do |key, value|
        style = create_style(value)
        styles[key] = style
      end
    end

    def self.create_style(hash)
      style = Java::limelight.ui.FlatStyle.new
      hash.each_pair do |key, value|
        value = value.to_s.gsub("pigpen", "#")
        style.send((key.to_s + "=").to_sym, value)
      end
      return style
    end

  end
  
end