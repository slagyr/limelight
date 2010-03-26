text_section {
  width "100%"
  bottom_border_width 1
  bottom_padding 10
}

section_header {
  width "100%" 
  background_color :light_blue
  secondary_background_color :white
  gradient :on
  gradient_angle 270
  vertical_alignment :center
}

section_title {
  font_size 20
  font_style "bold"
  padding 10
}

control_panel {
  width :greedy
  horizontal_alignment :right
}

control_link {
  padding 10
  text_color :grey
  hover {
    text_color :orange
  }
}

styled_text {
  width "100%"
  padding 10
  font_size 15
  horizontal_alignment :center
}

big_n_bold {
  font_size 25
  font_style "bold"
  font_face "Arial Black"
}

fox {
  font_face "Trajan Pro"
  text_color "orange"
  font_size 20
}

quick {     
  font_face "Handwriting - Dakota"
  text_color "#29d9f9"
  font_size 20
  font_style "italic"
}

brown {
  text_color :brown
  font_size 15
  font_style "bold"
}

jumped {
  font_face "Eccentric Std"
  text_color :grey
  font_size 30
}

over {
  font_face "Lithos Pro"
  text_color "#3b5612"
  font_size 20

}

lazy {
  font_face "Cooper Std"
  text_color :maroon
  font_size 25
  font_style "bold"
}

dog {
  font_face "Bank Gothic"
  text_color :tan
  font_size 25
}

courier {
  font_face "courier new"
}

bolded {
  font_style "bold"
}

green {
  text_color "dark green"
}

big_n_blue {
  font_size 20
  font_style "bold"
  font_face "Arial Black"
  text_color :blue
}

background_color {
  text_color :orange
  background_color :blue
}

bordered {
  border_color :grey
  border_width 1
  rounded_corner_radius 4
}

padded {
  padding 15
}

background_image {
  background_image "images/logo.png"
  text_color :red
  font_size 30
  background_image_fill_strategy :scale
}
