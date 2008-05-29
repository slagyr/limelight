#- Copyright 2008 8th Light, Inc.
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
  section_label :text => "URL:."
  field_wrapper do
    url_field :players => "text_box", :width => "500", :id => "url_field"
  end
  download_button :text => "Download"
end
section do
  section_title :text => "3. Open the Limelight Sandbox Production, Full of Examples."
  sandbox_button :text => "Open the Sandbox"
end
spacer :width => "100", :height => "110"
copyright :text => "Copyright 2008 8th Light, Inc."