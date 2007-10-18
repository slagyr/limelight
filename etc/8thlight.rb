$: << File.expand_path(File.dirname(__FILE__) + "/../ruby_src/lib")
require 'block'
require 'page'
require 'styles'
require 'llm_parser'

parser = LlmParser.new
page = parser.parse(IO.read("8thlight.llm"))

# IMAGES = File.expand_path("~/Projects/8thlight.com/public/images")
# 
# tag_line_text = <<END
# We at 8th Light believe craftsmanship and quality are the path to success.
# 
# We specialize in writing high quality custom applications.
# END
# 
# page = Page.new(:name => "page")
# main_column = page << Block.new(:name => "main_column")
# header = main_column << Block.new(:name => "header")
# menu = main_column << Block.new(:name => "menu")
# ["Home", "Services", "About", "Blog", "Contact"].each do |text|
#   link = menu << Block.new(:name => "link", :text => text)
#   link.on_mouse_enter { link.style :text_color => "#ffffff", :background_image => "#{IMAGES}/button_bg.jpg" }
#   link.on_mouse_exit { link.style :text_color => "#0049F4", :background_image => "#{IMAGES}/button_bg_ie.jpg" }
# end
# tag_line = main_column << Block.new(:name => "tag_line")
# tag_line_content = tag_line << Block.new(:name => "tag_line_content", :text => tag_line_text)
#   
# spot_light = main_column << Block.new(:name => "spot_light")
# spot_light_title = spot_light << Block.new(:name => "spot_light_title", :text => "In the spotlight")
# 
# book_news = spot_light << Block.new(:name => "book_news")
# book_news_img = book_news << Block.new(:name => "book_news_img")
# book_news_text = book_news << Block.new(:name => "book_news_text", :text => "Micah coathors Agile Principles, Patterns, and Practices in C# with Unclebob.")
# 
# sm_news = spot_light << Block.new(:name => "sm_news")
# sm_news_img = sm_news << Block.new(:name => "sm_news_img")
# sm_news_text = sm_news << Block.new(:name => "sm_news_text", :text => "Check out the Statemachine Ruby Gem and Rails plugin.")
# 
# news = main_column << Block.new(:name => "news")
# news_title = news << Block.new(:name => "news_title", :text => "Recent News")
# 
# doug_news = news << Block.new(:name => "doug_news", :text => "07.07  8th Light welcomes Doug Bradbury to the team!")
# doug_news = news << Block.new(:name => "rails_fest_news", :text => "07.07  8th Light doing Agile Rails Fest at Agile Conference in August.")
# doug_news = news << Block.new(:name => "eric_news", :text => "02.07  Eric Smith Joins the 8th Light team!")
# doug_news = news << Block.new(:name => "jim_news", :text => "01.07  Jim Suchy Joins the 8th Light team!")
# doug_news = news << Block.new(:name => "wpt_news", :text => "01.07  Micah Martin contributes article on FitNesse to the just released \"Windows Developer Power Tools\" book.")
# 
# footer = main_column << Block.new(:name => "footer")
# copyright = footer << Block.new(:name => "copyright", :text => "8th Light, Inc. Copyright 2007")

Styles.load_into_page(File.expand_path(File.dirname(__FILE__) + "/8thlight.style"), page)
page.loadStyle()

book = Book.new()
book.open(page)
