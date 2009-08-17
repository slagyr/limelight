#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

__ :name => "root"
welcome :text => "Welcome to..."
logo
intro :text => "From here you may..."
section do
  section_title :text => "1. Open a Production on Your Computer"
  browse_button :text => "Browse My Computer"
end
section do
  section_title :text => "2. Download and Open a Production From the Internet"
  section_label :text => "URL:"
  url_field :players => "text_box",  :id => "url_field"
  download_button :text => "Download"
end
section do
  section_title :text => "3. Open the Limelight Sandbox Production, Full of Examples."
  sandbox_button :text => "Open the Sandbox"
end
spacer :width => "100", :height => "110"
copyright :text => "Copyright 2008-2009 8th Light, Inc."