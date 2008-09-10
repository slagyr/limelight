#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Clip

  def mouse_clicked(e)
    play_sound("sounds/#{text}.au")
  end

end