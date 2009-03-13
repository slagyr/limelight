#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module StyleTable
  
  def populate(style)
    find_by_name('style_value').each do |style_value|
      style_value.populate(style)
    end
  end
  
end