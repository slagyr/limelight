#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

root {
  width "900"
  height "800"
  background_image "images/splash.png"
}

welcome {
  top_padding 50
  left_padding 50
  font_size 50
  font_face "Brush Script MT"
  text_color :white
  width "100%"
}

logo {
  left_margin 100
  width 500
  height 110
  background_image "images/logo.png"
  background_image_fill_strategy :static
}

intro {
  top_margin 25
  left_margin 20
  width "100%"
  text_color :white
  font_face "Arial Rounded MT Bold"
  font_style "italic"
  font_size 25
}

section {
  width "100%"
  margin 20
  rounded_corner_radius 20
  background_color "#fffa"
  secondary_background_color "#fff6"
  gradient :on
  gradient_angle 270
  padding 10
  horizontal_alignment :center
}

section_title {
  width "100%"
  horizontal_alignment :center
  font_size 25
  font_face "Arial Rounded MT Bold"
  text_color "#666"
}

section_label {
  text_color :white
  font_face "Arial Rounded MT Bold"
  font_size 25
}

copyright {
  width "100%"
  text_color :white
  font_face "Times New Roman"
  font_size "14"
  horizontal_alignment :center
}

browse_button {
  horizontal_alignment :center
  width "100%"
  text_color :white
  font_face "Arial Rounded MT Bold"
  font_style "italic"
  font_size 25
  hover {
    text_color "#67b313"
  }
}

url_field {
  top_padding 5
  width 500
  height 30
}

download_button {
  extends "browse_button"
  width :auto
  left_padding 5
  hover {
    text_color "#67b313"
  }
}

sandbox_button{
  extends "browse_button"
  hover {
    text_color "#67b313"
  }
}
