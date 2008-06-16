#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'yaml'

module Limelight
  
  class Styles

    def self.load(content)
      yamalized_content = content.gsub("\t", "  ").gsub("#", "pigpen")
      return YAML.load(yamalized_content)
    end

    def self.load_into_scene(filename, scene)
      content = scene.loader.load(filename)
      style_defs = load(content)
      styles = scene.styles;
  
      style_defs.each_pair do |key, value|
        style = create_style(value)
        styles[key] = style
      end
    end

    def self.create_style(hash)
      style = Styles::FlatStyle.new
      hash.each_pair do |key, value|
        value = value.to_s.gsub("pigpen", "#")
        style.send((key.to_s + "=").to_sym, value)
      end
      return style
    end

  end
  
end