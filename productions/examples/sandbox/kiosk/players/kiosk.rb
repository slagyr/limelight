#- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

prop_reader :kiosk_button, :fullscreen_button

def toggle_kiosk_mode
  if stage.kiosk?
    kiosk_button.text = "Toggle Kiosk Mode: OFF"
    stage.kiosk = false
  else
    kiosk_button.text = "Toggle Kiosk Mode: ON"
    stage.kiosk = true
  end
end

def toggle_fullscreen_mode
  if stage.fullscreen?
    fullscreen_button.text = "Toggle Fullscreen Mode: OFF"
    stage.fullscreen = false
  else
    fullscreen_button.text = "Toggle Fullscreen Mode: ON"
    stage.fullscreen = true
  end
end

def hide_a_bit
  stage.hide
  sleep(3)
  stage.show
end

#  def allow_close?
#    puts "allow_close? called"
#    return false
#  end
