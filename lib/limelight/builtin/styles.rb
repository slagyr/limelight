#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

limelight_builtin_players_curtains {
  float :on
  x 0
  y 0
  width "100%"
  height "100%"
  background_color "#FFF0"
}

limelight_builtin_players_combo_box_popup_list {
  float "on"
  background_color "#EEED"
  rounded_corner_radius 5
  border_width 1
  border_color "#dcdcdc"
  vertical_scrollbar :on
  min_height 50
  max_height 200
}

limelight_builtin_players_combo_box_popup_list_item {
  width "100%"
  padding 3
  left_padding 10
  hover {
    text_color :white
    background_color "#bbd453"
    secondary_background_color "#9fb454"
    gradient_angle 270
    gradient :on
  }
}