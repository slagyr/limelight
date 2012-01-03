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

on_value_changed do
  print("value changed")
end

backstage_reader :drop_down

def print(value)
  log = scene.find("drop_down_log")
  return if log.nil?
  log.text += value + "\n"

  results = scene.find("drop_down_results")
  results.text = "Selected value: #{drop_down.value}"
end
