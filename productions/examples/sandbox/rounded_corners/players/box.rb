#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Box
  
  def mouse_clicked(e)
    style.border_width = (style.top_border_width.to_i + 2).to_s
    update
  end
  
end