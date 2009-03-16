#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

inspector {
  width "100%"
  height "100%"
  background_color "#e8e8e8"
}

tool_bar {
  width "100%"
  height :auto
  border_color "#8e8e8e"
  bottom_border_width 1
}

tool {
  width 40
  height 40
  border_color "#8e8e8e"
  right_border_width 1
  text_color :black
  font_face :arial
  font_size 12
  horizontal_alignment :center
  vertical_alignment :center
  hover {
    border_color "#0638CC"
    border_width "2"
    background_color "#B8C6F2"
    text_color "black"
    font_style "bold"
  }
}

prop_tree {
 width "100%"
 height "200" 
 background_color :white
}

prop_row {
  width "100%"
  height "15"
  background_color :white
}

style_table {
  width "100%"
  height "68%"
}

style_row {
  width "100%"
  height "28"
}

style_name {
  width "50%"
  height "100%"
  vertical_alignment :center
  horizontal_alignment :right
  horizontal_scrollbar :off
  vertical_scrollbar :off
}

style_value {
  width "50%"
  vertical_alignment :center
  horizontal_alignment :left
}