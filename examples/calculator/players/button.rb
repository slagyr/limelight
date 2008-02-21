module Button
  
  def self.extended(extended_block)
    puts "Button extended"
  end
  
  def mouse_clicked(e)
    screen = page.find('screen')
    screen.text = self.text
    screen.style.background_color = "yellow"
    screen.update
    puts "clicked"
  end
end