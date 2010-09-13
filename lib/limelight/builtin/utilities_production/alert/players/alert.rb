#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

prop_reader :message

on_scene_opened do
  if message.bounds.height >= message.style.max_height.to_i
    message.style.vertical_scrollbar = :on
  end
end