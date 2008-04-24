module Sample
  def mouse_clicked(e)
    puts "mouse clicked"
    selection = scene.find('the_selection')
    selection.style.background_color = self.style.background_color
    selection.update
  end
end