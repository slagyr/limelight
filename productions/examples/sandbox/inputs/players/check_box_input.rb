#- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

on_key_pressed do |e|
  print("#{e.key_code} pressed")
end

on_char_typed do |e|
  print("#{e.char} typed")
end

on_key_released do |e|
  print("#{e.key_code} released")
end

on_focus_gained do
  print("gained focused")
end

on_focus_lost do
  print("lost focus")
end

on_button_pushed do
  print("pressed")
end

def print(value)
  log = scene.find("check_box_log")
  log.text += value + "\n"
  log.update

  results = scene.find("check_box_results")
  results.text = self.checked? ? "checked" : "unchecked"
  results.update
end
