module Button
  def mouseClicked
    screen = page.find('screen')
    screen.text = self.text
    screen.style.background_color = "yellow"
    screen.update
  end
end