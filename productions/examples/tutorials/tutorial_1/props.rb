#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

backdrop do
  %w{ red orange yellow green blue magenta }.each do |color|
    sample :background_color => color, :text => color
  end
  selection :id => "the_selection"
end