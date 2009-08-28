#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

# This file, (styles.rb) inside a scene, should define any styles specific to the containing scene.
# It makes use of the StyleBuilder DSL.
#
# For more information see: http://limelightwiki.8thlight.com/index.php/A_Cook%27s_Tour_of_Limelight#Styling_with_styles.rb
# For a complete listing of style attributes see: http://limelightwiki.8thlight.com/index.php/Style_Attributes

default_scene {
  background_color :black
  horizontal_alignment :center
  vertical_alignment :center
  width "100%"
  height "100%"
}

root {
  text_color :white
  font_size 18
}

close_button {
  text_color :blue
  border_color :blue
  padding 5
  border_width 1
  hover {
    text_color :white
    border_color :white
  }
}