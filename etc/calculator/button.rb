module Button
  
  def extended
    puts "Button extended"
  end
  
  def mouseClicked
    screen = page.find('screen')
    screen.text = self.text
    screen.style.background_color = "yellow"
    screen.update
    puts "clicked"
  end
end