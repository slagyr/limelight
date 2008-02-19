module StartButton
  
  def mouse_clicked(e)
    antville = page.find('antville')
    antville.restart
    text = "Running."
  end
end
