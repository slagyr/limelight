#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module PropRow
  
  attr_accessor :prop
  
  def mouse_clicked(e)
    production.controller.prop_selected(@prop)
  end
  
end