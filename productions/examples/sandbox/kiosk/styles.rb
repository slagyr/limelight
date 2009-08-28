#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

# This file, (styles.rb) inside a scene, should define any styles specific to the containing scene.
# It makes use of the StyleBuilder DSL.
#
# For more information see: http://limelightwiki.8thlight.com/index.php/A_Cook%27s_Tour_of_Limelight#Styling_with_styles.rb
# For a complete listing of style attributes see: http://limelightwiki.8thlight.com/index.php/Style_Attributes

kiosk {
  background_color :black
  horizontal_alignment :center
  vertical_alignment :top
  width "100%"
  height "100%"
}

action {
  width "100%"
  height 200
  margin 50
  left_margin "30%"
  right_margin "30%"
  background_color :white
  border_color :gray
  border_width 2
  rounded_corner_radius 10
  horizontal_alignment :center
  vertical_alignment :center
  text_color :gray
  font_size 20
  hover {
    secondary_background_color :blue
    gradient :on
  }

}