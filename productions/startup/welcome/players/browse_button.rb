#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module BrowseButton

  def mouse_clicked(e)
    scene.open_chosen_production
  end

end