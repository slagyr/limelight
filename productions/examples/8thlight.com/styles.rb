#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

page {
  width "100%"
  height "100%"
  horizontal_alignment "center"
  background_image "images/bg.jpg"
  vertical_scrollbar :on
}

main_column {
  border_width 0
  background_image "images/canvas_bg.jpg"
  width 823
  height :auto
  horizontal_alignment "center"
}

header {
  height 200
  background_image "images/header.jpg"
  width 823
  horizontal_alignment "center"
}

menu {
  height 67
  top_border_width 1
  bottom_border_width 1
  border_color "#333333"
  width 823
  horizontal_alignment :right
  right_margin 25
  left_margin 25
  bottom_margin 15
}

link {
  width 80
  height 50
  text_color "#0049F4"
  font_face "Arial"
  font_size 13
  alignment :center
  hover {
    text_color "#ffffff"
    background_image "images/button_bg.jpg"
  }
}

tag_line {
  width 823
  height 70
  background_image "images/anvil.jpg"
  right_margin 25
  left_margin 25
  horizontal_alignment :left
  font_style "italic"
  text_color "white"
  font_size 18
  top_padding 10
  left_padding 10
  right_padding 375
}

services_tagline {
  extends :tag_line
  background_image "images/botticelli.jpg"
  horizontal_alignment "right"
}

about_tagline {
  extends :tag_line
  background_image "images/moses.jpg"
  horizontal_alignment "right"
}

section_title {
  width 823
  height 50
  top_margin 20
  left_margin 50
  right_margin 50
  bottom_border_width 1
  border_color "#0049F4"
  text_color "white"
  font_size 20
  font_style "bold"
}

sub_title {
  text_color "#0049F4"
  font_size 32
  font_face "Courier"
}

section_body {
  width 823
  height :auto
  left_margin 50
  right_margin 50
  text_color "white"
  font_size 14
}

spot_light {
  width 412
  height 300
  top_margin 20
  left_margin 50
  right_margin 50
}

spot_light_title {
  width 312
  height 45
  bottom_border_width 1
  border_color "#0049F4"
  font_size 20
  text_color "white"
}

book_news {
  height 75
  width "100%"
  bottom_border_width 1
  border_color "#333333"
  top_margin 10
}

book_news_img {
  background_image "images/thumbnail_book.jpg"
  width 59
  height 58
}

book_news_text {
  width 253
  height 58
  left_margin 10
  font_size 15
  font_face "Helvetica"
  text_color "#919191"
}

sm_news {
  height 75
  width "100%"
  bottom_border_width 1
  border_color "#333333"
  top_margin 10
}

sm_news_img {
  background_image "images/statemachine_thumbnail.png"
  width 59
  height 58
}

sm_news_text {
  extends :book_news_text
}

news {
  width 411
  height 320
  top_margin 20
  left_margin 30
  right_margin 50
}

news_title {
  width 311
  height 45
  bottom_border_width 1
  border_color "#0049F4"
  font_size 20
  text_color "white"
}

new_item {
  text_color "#919191"
  font_size 12
  font_face "Helvetica"
  height 50
  width "100%"
  bottom_border_width 1
  border_color "#333333"
  top_margin 10
  background_color "black"
  hover {
    text_color "white"
    background_color "black"
  }
}

footer {
  background_image "images/footer_bg.jpg"
  height 80
  width 823
}

copyright {
  top_margin 40
  alignment :center
  text_color "#666"
  width 823
  height 80
}
