require 'yaml'

module Limelight
  
  class Styles

    def self.load(filename)
      content = IO.read(filename)
      yamalized_content = content.gsub("\t", "  ").gsub("#", "pigpen")
      return YAML.load(yamalized_content)
    end

    def self.load_into_page(filename, page)
      style_defs = load(filename)
      styles = page.getStyles();
  
      style_defs.each_pair do |key, value|
        style = create_style(value)
        styles.put(key, style)
      end
    end

    def self.create_style(hash)
      style = Style.new
      hash.each_pair do |key, value|
        value = value.to_s.gsub("pigpen", "#")
        style.send((key.to_s + "=").to_sym, value)
      end
      return style
    end

  end
  
end