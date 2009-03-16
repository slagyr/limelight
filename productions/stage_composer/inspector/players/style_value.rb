#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module StyleValue
  
  attr_accessor :descriptor
  
  def populate(style)
    self.text = style.get(@descriptor)
  end
  
  def focus_lost(e)
    production.controller.value_changed(@descriptor, text)
  end
  
end