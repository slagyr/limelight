#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

__ :name => "sandbox"
__install "header.rb"
arena do
  pattern do
    fader :background_color => "#0000", :text => "0% Alpha"
    fader :background_color => "#0008", :text => "50% Alpha"
    fader :background_color => "#000B", :text => "75% Alpha"
    fader :background_color => "#000D", :text => "88% Alpha"
    fader :background_color => "#000F", :text => "100% Alpha"
  end
end