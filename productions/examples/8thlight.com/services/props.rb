#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

__ :name => "page"
main_column do
  __install "menu.rb"
  services_tagline :text => "Pleasure in the job puts perfection in the work.\n\tAristotle"
  section_title :text => "services"
  section_body :text => __loader__.read("services/services.txt")
  __install "footer.rb"
end