__install "header.rb"
arena do
  action :id => "kiosk_button", :text => "Toggle Kiosk Mode: OFF", :on_mouse_clicked => "scene.toggle_kiosk_mode"
  action :id => "fullscreen_button", :text => "Toggle Fullscreen Mode: OFF", :on_mouse_clicked => "scene.toggle_fullscreen_mode"
  action :id => "hide_button", :text => "Hide for 3 Seconds", :on_mouse_clicked => "scene.hide_a_bit"
end