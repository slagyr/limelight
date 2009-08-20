#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

__install "header.rb"
arena do
  action :id => "kiosk_button", :text => "Toggle Kiosk Mode: OFF", :on_mouse_clicked => "scene.toggle_kiosk_mode"
  action :id => "fullscreen_button", :text => "Toggle Fullscreen Mode: OFF", :on_mouse_clicked => "scene.toggle_fullscreen_mode"
  action :id => "hide_button", :text => "Hide for 3 Seconds", :on_mouse_clicked => "scene.hide_a_bit"
end