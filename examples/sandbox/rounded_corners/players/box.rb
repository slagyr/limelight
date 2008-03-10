module Box
  
  def mouse_clicked(e)
    style.border_width = (style.top_border_width.to_i + 2).to_s
    update
  end
  
end