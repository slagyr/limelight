on_mouse_clicked do
  lcd = scene.find("lcd")
  if text == "c"
    lcd.text = ""
  elsif text == "="
    lcd.text = eval(lcd.text)
  else
    lcd.text += self.text
  end
end