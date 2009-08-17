title :id => "title", :text => "Limelight Alert"
advise :id => "message", :text => @message, :horizontal_alignment => :center
buttons do
  button :id => "ok_button", :text => "OK", :players => "button", :on_button_pressed => "production.process_alert_response(true)"
end
