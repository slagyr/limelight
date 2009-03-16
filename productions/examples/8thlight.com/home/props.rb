#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

__ :name => "page"
main_column do
  __install "menu.rb"
  tag_line
  spot_light do
    spot_light_title :text => "In the spotlight"
    book_news do
      book_news_img
      book_news_text :text => "Micah coathors Agile Principles, Patterns, and Practices in C# with Unclebob."
    end
    sm_news do
      sm_news_img
      sm_news_text :text => "Check out the Statemachine Ruby Gem and Rails plugin."
    end
  end
  news do
    news_title :text => "Recent News"
    new_item :text => "07.07  8th Light welcomes Doug Bradbury to the team!"
    new_item :text => "07.07  8th Light doing Agile Rails Fest at Agile Conference in August."
    new_item :text => "02.07  Eric Smith Joins the 8th Light team!"
    new_item :text => "01.07  Jim Suchy Joins the 8th Light team!"
    new_item :text => "01.07  Micah Martin contributes article on FitNesse to the just released \"Windows Developer Power Tools\" book."
  end
  __install "footer.rb"
end