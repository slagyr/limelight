#- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

arena {
  vertical_alignment :top
  vertical_scrollbar :on
}

control_panel {
  width "100%"
  background_color :light_blue
  secondary_background_color :white
  gradient_angle 270
  gradient :on
  horizontal_alignment :center
  padding 10
  bottom_border_width 1
  border_color :black
}

setting {
  width 120
}

label {
  text_color :white
  font_style "bold"
}

input {
  width :auto
}

container {
  border_color :black
  border_width 1
  width "80%"
  min_height 100
  top_margin 50
}

actions {
  width "100%"
  horizontal_alignment :center
}

child {
  border_width 1
  border_color :grey
  vertical_alignment :center
  horizontal_alignment :center
}
