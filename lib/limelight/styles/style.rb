#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

raise "studio.rb is present for solely to document it's Java counterpart limelight.Studio.  This file should NOT be loaded in the Ruby runtime."

module Limelight
  module Styles
    class Style
      # Specifies the Width attribute of a prop.
      #   type:           dimension
      #   default_value:  auto
      attr_accessor :width

      # Specifies the Height attribute of a prop.
      #   type:           dimension
      #   default_value:  auto
      attr_accessor :height

      # Specifies the Min Width attribute of a prop.
      #   type:           noneable simple dimension
      #   default_value:  none
      attr_accessor :min_width

      # Specifies the Min Height attribute of a prop.
      #   type:           noneable simple dimension
      #   default_value:  none
      attr_accessor :min_height

      # Specifies the Max Width attribute of a prop.
      #   type:           noneable simple dimension
      #   default_value:  none
      attr_accessor :max_width

      # Specifies the Max Height attribute of a prop.
      #   type:           noneable simple dimension
      #   default_value:  none
      attr_accessor :max_height

      # Specifies the Vertical Scrollbar attribute of a prop.
      #   type:           on/off
      #   default_value:  off
      attr_accessor :vertical_scrollbar

      # Specifies the Horizontal Scrollbar attribute of a prop.
      #   type:           on/off
      #   default_value:  off
      attr_accessor :horizontal_scrollbar

      # Specifies the Top Border Color attribute of a prop.
      #   type:           color
      #   default_value:  #000000ff
      attr_accessor :top_border_color

      # Specifies the Right Border Color attribute of a prop.
      #   type:           color
      #   default_value:  #000000ff
      attr_accessor :right_border_color

      # Specifies the Bottom Border Color attribute of a prop.
      #   type:           color
      #   default_value:  #000000ff
      attr_accessor :bottom_border_color

      # Specifies the Left Border Color attribute of a prop.
      #   type:           color
      #   default_value:  #000000ff
      attr_accessor :left_border_color

      # Specifies the Top Border Width attribute of a prop.
      #   type:           pixels
      #   default_value:  0
      attr_accessor :top_border_width

      # Specifies the Right Border Width attribute of a prop.
      #   type:           pixels
      #   default_value:  0
      attr_accessor :right_border_width

      # Specifies the Bottom Border Width attribute of a prop.
      #   type:           pixels
      #   default_value:  0
      attr_accessor :bottom_border_width

      # Specifies the Left Border Width attribute of a prop.
      #   type:           pixels
      #   default_value:  0
      attr_accessor :left_border_width

      # Specifies the Top Margin attribute of a prop.
      #   type:           pixels
      #   default_value:  0
      attr_accessor :top_margin

      # Specifies the Right Margin attribute of a prop.
      #   type:           pixels
      #   default_value:  0
      attr_accessor :right_margin

      # Specifies the Bottom Margin attribute of a prop.
      #   type:           pixels
      #   default_value:  0
      attr_accessor :bottom_margin

      # Specifies the Left Margin attribute of a prop.
      #   type:           pixels
      #   default_value:  0
      attr_accessor :left_margin

      # Specifies the Top Padding attribute of a prop.
      #   type:           pixels
      #   default_value:  0
      attr_accessor :top_padding

      # Specifies the Right Padding attribute of a prop.
      #   type:           pixels
      #   default_value:  0
      attr_accessor :right_padding

      # Specifies the Bottom Padding attribute of a prop.
      #   type:           pixels
      #   default_value:  0
      attr_accessor :bottom_padding

      # Specifies the Left Padding attribute of a prop.
      #   type:           pixels
      #   default_value:  0
      attr_accessor :left_padding

      # Specifies the Background Color attribute of a prop.
      #   type:           color
      #   default_value:  #00000000
      attr_accessor :background_color

      # Specifies the Secondary Background Color attribute of a prop.
      #   type:           color
      #   default_value:  #00000000
      attr_accessor :secondary_background_color

      # Specifies the Background Image attribute of a prop.
      #   type:           noneable string
      #   default_value:  none
      attr_accessor :background_image

      # Specifies the Background Image Fill Strategy attribute of a prop.
      #   type:           fill strategy
      #   default_value:  repeat
      attr_accessor :background_image_fill_strategy

      # Specifies the Gradient attribute of a prop.
      #   type:           on/off
      #   default_value:  off
      attr_accessor :gradient

      # Specifies the Gradient Angle attribute of a prop.
      #   type:           degrees
      #   default_value:  90
      attr_accessor :gradient_angle

      # Specifies the Gradient Penetration attribute of a prop.
      #   type:           percentage
      #   default_value:  100%
      attr_accessor :gradient_penetration

      # Specifies the Cyclic Gradient attribute of a prop.
      #   type:           on/off
      #   default_value:  off
      attr_accessor :cyclic_gradient

      # Specifies the Horizontal Alignment attribute of a prop.
      #   type:           horizontal alignment
      #   default_value:  left
      attr_accessor :horizontal_alignment

      # Specifies the Vertical Alignment attribute of a prop.
      #   type:           vertical alignment
      #   default_value:  top
      attr_accessor :vertical_alignment

      # Specifies the Text Color attribute of a prop.
      #   type:           color
      #   default_value:  #000000ff
      attr_accessor :text_color

      # Specifies the Font Face attribute of a prop.
      #   type:           string
      #   default_value:  Arial
      attr_accessor :font_face

      # Specifies the Font Size attribute of a prop.
      #   type:           integer
      #   default_value:  12
      attr_accessor :font_size

      # Specifies the Font Style attribute of a prop.
      #   type:           font style
      #   default_value:  plain
      attr_accessor :font_style

      # Specifies the Transparency attribute of a prop.
      #   type:           percentage
      #   default_value:  0%
      attr_accessor :transparency

      # Specifies the Top Right Rounded Corner Radius attribute of a prop.
      #   type:           pixels
      #   default_value:  0
      attr_accessor :top_right_rounded_corner_radius

      # Specifies the Bottom Right Rounded Corner Radius attribute of a prop.
      #   type:           pixels
      #   default_value:  0
      attr_accessor :bottom_right_rounded_corner_radius

      # Specifies the Bottom Left Rounded Corner Radius attribute of a prop.
      #   type:           pixels
      #   default_value:  0
      attr_accessor :bottom_left_rounded_corner_radius

      # Specifies the Top Left Rounded Corner Radius attribute of a prop.
      #   type:           pixels
      #   default_value:  0
      attr_accessor :top_left_rounded_corner_radius

      # Specifies the Top Right Border Width attribute of a prop.
      #   type:           pixels
      #   default_value:  0
      attr_accessor :top_right_border_width

      # Specifies the Bottom Right Border Width attribute of a prop.
      #   type:           pixels
      #   default_value:  0
      attr_accessor :bottom_right_border_width

      # Specifies the Bottom Left Border Width attribute of a prop.
      #   type:           pixels
      #   default_value:  0
      attr_accessor :bottom_left_border_width

      # Specifies the Top Left Border Width attribute of a prop.
      #   type:           pixels
      #   default_value:  0
      attr_accessor :top_left_border_width

      # Specifies the Top Right Border Color attribute of a prop.
      #   type:           color
      #   default_value:  #000000ff
      attr_accessor :top_right_border_color

      # Specifies the Bottom Right Border Color attribute of a prop.
      #   type:           color
      #   default_value:  #000000ff
      attr_accessor :bottom_right_border_color

      # Specifies the Bottom Left Border Color attribute of a prop.
      #   type:           color
      #   default_value:  #000000ff
      attr_accessor :bottom_left_border_color

      # Specifies the Top Left Border Color attribute of a prop.
      #   type:           color
      #   default_value:  #000000ff
      attr_accessor :top_left_border_color

      # Specifies the Float attribute of a prop.
      #   type:           on/off
      #   default_value:  off
      attr_accessor :float

      # Specifies the X attribute of a prop.
      #   type:           x-coordinate
      #   default_value:  0
      attr_accessor :x

      # Specifies the Y attribute of a prop.
      #   type:           y-coordinate
      #   default_value:  0
      attr_accessor :y

      # Specifies the Background Image X attribute of a prop.
      #   type:           x-coordinate
      #   default_value:  0
      attr_accessor :background_image_x

      # Specifies the Background Image Y attribute of a prop.
      #   type:           y-coordinate
      #   default_value:  0
      attr_accessor :background_image_y

    end
  end
end
