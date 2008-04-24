module PropRow
  
  attr_accessor :prop
  
  def mouse_clicked(e)
    production.controller.prop_selected(@prop)
  end
  
end