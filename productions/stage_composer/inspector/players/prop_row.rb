#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module PropRow
  
  attr_accessor :prop
  
  def mouse_clicked(e)
    production.controller.prop_selected(@prop)
  end
  
end