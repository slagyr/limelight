#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

backdrop do
  %w{ red orange yellow green blue magenta }.each do |color|
    sample :background_color => color, :text => color
  end
  selection :id => "the_selection"
end