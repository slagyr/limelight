#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

def open
  puts "Curtains.open"
  scene.remove(self)
end

on_mouse_clicked do
  open
end

