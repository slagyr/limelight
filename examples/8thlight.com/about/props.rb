__ :class_name => "page"
main_column do
  __install "menu.rb"
  about_tagline :text => "By the work one knows the workmen.\nJean De La Fontaine"
  section_title :text => "About Us"
  section_body :text => __loader__.read("about/about.txt")
  __install "footer.rb"
end