#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module SandboxButton

  def mouse_clicked(e)
    sandbox_path = File.join($LIMELIGHT_HOME, "productions", "examples", "sandbox")
    scene.open_production(sandbox_path)
  end

end