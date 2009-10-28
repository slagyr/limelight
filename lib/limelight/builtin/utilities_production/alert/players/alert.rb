#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Alert #:nodoc:

  prop_reader :message

  def scene_opened(e)
    if message.area.height == message.style.max_height.to_i
      message.style.vertical_scrollbar = :on
    end
  end

end