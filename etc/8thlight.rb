require 'java'
require File.expand_path(File.dirname(__FILE__) + "/../dist/limelight.jar")

JBlock = Java::limelight.Block
JPage = Java::limelight.Page
Book = Java::limelight.Book

class Page < JPage
  
  def initialize(options = {})
    super()
    options.each_pair do |key, value|
      value = value.to_s if value.is_a?(Symbol)
      self.send((key.to_s + "=").to_s, value)
    end
  end

  def <<(value)
    add(value)
    return value
  end
  
end

class Block < JBlock
  
  def initialize(options = {})
    super()
    options.each_pair do |key, value|
      value = value.to_s if value.is_a?(Symbol)
      self.send((key.to_s + "=").to_s, value)
    end
  end

  def <<(value)
    add(value)
    return value
  end
  
end

IMAGES = File.expand_path("~/Projects/8thlight.com/public/images")

module Eighthlight
  def self.load()
    tag_line_text = <<END
We at 8th Light believe craftsmanship and quality are the path to success.
    
We specialize in writing high quality custom applications.
END
    page = Page.new(:width => "1000", :height => "900", :horizontal_alignment => :center, :background_image => "#{IMAGES}/bg.jpg")
    main_column = page << Block.new(:border_width => 0, :background_image => "#{IMAGES}/canvas_bg.jpg", :width => "823", :height => "850", :horizontal_alignment => :center)
    header = main_column << Block.new(:height => "200", :background_image => "#{IMAGES}/header.jpg", :width => "823", :horizontal_alignment => :center)
    menu = main_column << Block.new(:height => "67", :top_border_width => 1, :bottom_border_width => 1, :border_color => "#333333", :width => "823", :horizontal_alignment => :right, :right_margin => 25, :left_margin => 25, :bottom_margin => 15)
     ["Home", "Services", "About", "Blog", "Contact"].each do |text|
       link = menu << Block.new(:text => text, :width => "80", :height => "50", :text_color => "#0049F4", :font_face => "Arial", :font_size => 13, :vertical_alignment => :middle, :horizontal_alignment => :center)
       # link.on_mouse_enter { link.style :color => "#ffffff", :background_image => "#{IMAGES}/button_bg.jpg" }
       # link.on_mouse_leave { link.style :color => "#0049F4", :background_image => "#{IMAGES}/button_bg_ie.jpg" }
     end
    tag_line = main_column << Block.new(:width => "823", :height => "200", :background_image => "#{IMAGES}/tag_line.jpg", :right_margin => 25, :left_margin => 25, :horizontal_alignment => :left )
    tag_line_content = tag_line << Block.new(:text => tag_line_text, :width => "400", :height => "150", :top_margin => 50, :left_margin => 50, :text_color => :white, :font_size => 14)
    
    spot_light = main_column << Block.new(:width => "412", :height => "300", :top_margin => 20, :left_margin => 50, :right_margin => 50)
    spot_light_title = spot_light << Block.new(:text => "In the spotlight", :width => "312", :height => "35", :bottom_border_width => 1, :border_color => "#0049F4", :font_size => 20, :text_color => "white")
    news = main_column << Block.new(:width => "411", :height => "300", :top_margin => 20, :left_margin => 50, :right_margin => 50)
    spot_light_title = news << Block.new(:text => "Recent News", :width => "311", :height => "35", :bottom_border_width => 1, :border_color => "#0049F4", :font_size => 20, :text_color => "white")
    
    footer = main_column << Block.new(:background_image => "#{IMAGES}/footer_bg.jpg", :height => "80", :width => "823")
    copyright = footer << Block.new(:text => "8th Light, Inc. Copyright 2007", :top_margin => 40, :vertical_alignment => :middle, :horizontal_alignment => :center, :text_color => "#666", :width => "823", :height => "80")

    return page
  end
end

if __FILE__ == $0
  page = Eighthlight.load
  book = Book.new()
  book.open(page)
end