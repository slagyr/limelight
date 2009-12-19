#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

__ :name => "sandbox"
__install "header.rb"
arena do
  pattern do
    fader :background_color => "#0000", :text => "0% Alpha", :id => "fader1"
    fader :background_color => "#0008", :text => "50% Alpha", :id => "fader2"
    fader :background_color => "#000B", :text => "75% Alpha", :id => "fader3"
    fader :background_color => "#000D", :text => "88% Alpha", :id => "fader4"
    fader :background_color => "#000F", :text => "100% Alpha", :id => "fader5"
  end
end