module Button
  
  def self.extended(extended_block)
    puts "Button extended"
  end
  
  def mouse_clicked(e)
    screen = page.find('screen')
    if self.text != "="
      screen.text += self.text
      screen.style.background_color = "yellow"
      screen.update
    else
      result = eval(screen.text)
      screen.text = result.to_s
      screen.style.background_color = "white"
      screen.update
    end
    puts "clicked"
  end
end