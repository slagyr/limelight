#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

title :id => "title", :text => "Limelight Alert"
advise :id => "message", :text => @message
buttons do
  button :id => "ok_button", :text => "OK", :players => "button", :on_button_pushed => "production.process_alert_response(true)"
end
