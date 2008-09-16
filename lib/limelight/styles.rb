#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight

  # This module is for reference only.  The Style class is a java class for various reasons.
  #
  module Styles
    # Specifies the Width of a prop.
    #
    #   style.width = <value>
    #
    Width  = Limelight::Styles::Style::STYLE_LIST.get(0)

    # Specifies the Height of a prop.
    #
    #   style.height = <value>
    #
    Height  = Limelight::Styles::Style::STYLE_LIST.get(1)

    # Specifies the Min Width of a prop.
    #
    #   style.min_width = <value>
    #
    MinWidth  = Limelight::Styles::Style::STYLE_LIST.get(2)

    # Specifies the Min Height of a prop.
    #
    #   style.min_height = <value>
    #
    MinHeight  = Limelight::Styles::Style::STYLE_LIST.get(3)

    # Specifies the Max Width of a prop.
    #
    #   style.max_width = <value>
    #
    MaxWidth  = Limelight::Styles::Style::STYLE_LIST.get(4)

    # Specifies the Max Height of a prop.
    #
    #   style.max_height = <value>
    #
    MaxHeight  = Limelight::Styles::Style::STYLE_LIST.get(5)

    # Specifies the Vertical Scrollbar of a prop.
    #
    #   style.vertical_scrollbar = <value>
    #
    VerticalScrollbar  = Limelight::Styles::Style::STYLE_LIST.get(6)

    # Specifies the Horizontal Scrollbar of a prop.
    #
    #   style.horizontal_scrollbar = <value>
    #
    HorizontalScrollbar  = Limelight::Styles::Style::STYLE_LIST.get(7)

    # Specifies the Top Border Color of a prop.
    #
    #   style.top_border_color = <value>
    #
    TopBorderColor  = Limelight::Styles::Style::STYLE_LIST.get(8)

    # Specifies the Right Border Color of a prop.
    #
    #   style.right_border_color = <value>
    #
    RightBorderColor  = Limelight::Styles::Style::STYLE_LIST.get(9)

    # Specifies the Bottom Border Color of a prop.
    #
    #   style.bottom_border_color = <value>
    #
    BottomBorderColor  = Limelight::Styles::Style::STYLE_LIST.get(10)

    # Specifies the Left Border Color of a prop.
    #
    #   style.left_border_color = <value>
    #
    LeftBorderColor  = Limelight::Styles::Style::STYLE_LIST.get(11)

    # Specifies the Top Border Width of a prop.
    #
    #   style.top_border_width = <value>
    #
    TopBorderWidth  = Limelight::Styles::Style::STYLE_LIST.get(12)

    # Specifies the Right Border Width of a prop.
    #
    #   style.right_border_width = <value>
    #
    RightBorderWidth  = Limelight::Styles::Style::STYLE_LIST.get(13)

    # Specifies the Bottom Border Width of a prop.
    #
    #   style.bottom_border_width = <value>
    #
    BottomBorderWidth  = Limelight::Styles::Style::STYLE_LIST.get(14)

    # Specifies the Left Border Width of a prop.
    #
    #   style.left_border_width = <value>
    #
    LeftBorderWidth  = Limelight::Styles::Style::STYLE_LIST.get(15)

    # Specifies the Top Margin of a prop.
    #
    #   style.top_margin = <value>
    #
    TopMargin  = Limelight::Styles::Style::STYLE_LIST.get(16)

    # Specifies the Right Margin of a prop.
    #
    #   style.right_margin = <value>
    #
    RightMargin  = Limelight::Styles::Style::STYLE_LIST.get(17)

    # Specifies the Bottom Margin of a prop.
    #
    #   style.bottom_margin = <value>
    #
    BottomMargin  = Limelight::Styles::Style::STYLE_LIST.get(18)

    # Specifies the Left Margin of a prop.
    #
    #   style.left_margin = <value>
    #
    LeftMargin  = Limelight::Styles::Style::STYLE_LIST.get(19)

    # Specifies the Top Padding of a prop.
    #
    #   style.top_padding = <value>
    #
    TopPadding  = Limelight::Styles::Style::STYLE_LIST.get(20)

    # Specifies the Right Padding of a prop.
    #
    #   style.right_padding = <value>
    #
    RightPadding  = Limelight::Styles::Style::STYLE_LIST.get(21)

    # Specifies the Bottom Padding of a prop.
    #
    #   style.bottom_padding = <value>
    #
    BottomPadding  = Limelight::Styles::Style::STYLE_LIST.get(22)

    # Specifies the Left Padding of a prop.
    #
    #   style.left_padding = <value>
    #
    LeftPadding  = Limelight::Styles::Style::STYLE_LIST.get(23)

    # Specifies the Background Color of a prop.
    #
    #   style.background_color = <value>
    #
    BackgroundColor  = Limelight::Styles::Style::STYLE_LIST.get(24)

    # Specifies the Secondary Background Color of a prop.
    #
    #   style.secondary_background_color = <value>
    #
    SecondaryBackgroundColor  = Limelight::Styles::Style::STYLE_LIST.get(25)

    # Specifies the Background Image of a prop.
    #
    #   style.background_image = <value>
    #
    BackgroundImage  = Limelight::Styles::Style::STYLE_LIST.get(26)

    # Specifies the Background Image Fill Strategy of a prop.
    #
    #   style.background_image_fill_strategy = <value>
    #
    BackgroundImageFillStrategy  = Limelight::Styles::Style::STYLE_LIST.get(27)

    # Specifies the Gradient of a prop.
    #
    #   style.gradient = <value>
    #
    Gradient  = Limelight::Styles::Style::STYLE_LIST.get(28)

    # Specifies the Gradient Angle of a prop.
    #
    #   style.gradient_angle = <value>
    #
    GradientAngle  = Limelight::Styles::Style::STYLE_LIST.get(29)

    # Specifies the Gradient Penetration of a prop.
    #
    #   style.gradient_penetration = <value>
    #
    GradientPenetration  = Limelight::Styles::Style::STYLE_LIST.get(30)

    # Specifies the Cyclic Gradient of a prop.
    #
    #   style.cyclic_gradient = <value>
    #
    CyclicGradient  = Limelight::Styles::Style::STYLE_LIST.get(31)

    # Specifies the Horizontal Alignment of a prop.
    #
    #   style.horizontal_alignment = <value>
    #
    HorizontalAlignment  = Limelight::Styles::Style::STYLE_LIST.get(32)

    # Specifies the Vertical Alignment of a prop.
    #
    #   style.vertical_alignment = <value>
    #
    VerticalAlignment  = Limelight::Styles::Style::STYLE_LIST.get(33)

    # Specifies the Text Color of a prop.
    #
    #   style.text_color = <value>
    #
    TextColor  = Limelight::Styles::Style::STYLE_LIST.get(34)

    # Specifies the Font Face of a prop.
    #
    #   style.font_face = <value>
    #
    FontFace  = Limelight::Styles::Style::STYLE_LIST.get(35)

    # Specifies the Font Size of a prop.
    #
    #   style.font_size = <value>
    #
    FontSize  = Limelight::Styles::Style::STYLE_LIST.get(36)

    # Specifies the Font Style of a prop.
    #
    #   style.font_style = <value>
    #
    FontStyle  = Limelight::Styles::Style::STYLE_LIST.get(37)

    # Specifies the Transparency of a prop.
    #
    #   style.transparency = <value>
    #
    Transparency  = Limelight::Styles::Style::STYLE_LIST.get(38)

    # Specifies the Top Right Rounded Corner Radius of a prop.
    #
    #   style.top_right_rounded_corner_radius = <value>
    #
    TopRightRoundedCornerRadius  = Limelight::Styles::Style::STYLE_LIST.get(39)

    # Specifies the Bottom Right Rounded Corner Radius of a prop.
    #
    #   style.bottom_right_rounded_corner_radius = <value>
    #
    BottomRightRoundedCornerRadius  = Limelight::Styles::Style::STYLE_LIST.get(40)

    # Specifies the Bottom Left Rounded Corner Radius of a prop.
    #
    #   style.bottom_left_rounded_corner_radius = <value>
    #
    BottomLeftRoundedCornerRadius  = Limelight::Styles::Style::STYLE_LIST.get(41)

    # Specifies the Top Left Rounded Corner Radius of a prop.
    #
    #   style.top_left_rounded_corner_radius = <value>
    #
    TopLeftRoundedCornerRadius  = Limelight::Styles::Style::STYLE_LIST.get(42)

    # Specifies the Top Right Border Width of a prop.
    #
    #   style.top_right_border_width = <value>
    #
    TopRightBorderWidth  = Limelight::Styles::Style::STYLE_LIST.get(43)

    # Specifies the Bottom Right Border Width of a prop.
    #
    #   style.bottom_right_border_width = <value>
    #
    BottomRightBorderWidth  = Limelight::Styles::Style::STYLE_LIST.get(44)

    # Specifies the Bottom Left Border Width of a prop.
    #
    #   style.bottom_left_border_width = <value>
    #
    BottomLeftBorderWidth  = Limelight::Styles::Style::STYLE_LIST.get(45)

    # Specifies the Top Left Border Width of a prop.
    #
    #   style.top_left_border_width = <value>
    #
    TopLeftBorderWidth  = Limelight::Styles::Style::STYLE_LIST.get(46)

    # Specifies the Top Right Border Color of a prop.
    #
    #   style.top_right_border_color = <value>
    #
    TopRightBorderColor  = Limelight::Styles::Style::STYLE_LIST.get(47)

    # Specifies the Bottom Right Border Color of a prop.
    #
    #   style.bottom_right_border_color = <value>
    #
    BottomRightBorderColor  = Limelight::Styles::Style::STYLE_LIST.get(48)

    # Specifies the Bottom Left Border Color of a prop.
    #
    #   style.bottom_left_border_color = <value>
    #
    BottomLeftBorderColor  = Limelight::Styles::Style::STYLE_LIST.get(49)

    # Specifies the Top Left Border Color of a prop.
    #
    #   style.top_left_border_color = <value>
    #
    TopLeftBorderColor  = Limelight::Styles::Style::STYLE_LIST.get(50)

    # Specifies the Float of a prop.
    #
    #   style.float = <value>
    #
    Float  = Limelight::Styles::Style::STYLE_LIST.get(51)

    # Specifies the X of a prop.
    #
    #   style.x = <value>
    #
    X  = Limelight::Styles::Style::STYLE_LIST.get(52)

    # Specifies the Y of a prop.
    #
    #   style.y = <value>
    #
    Y  = Limelight::Styles::Style::STYLE_LIST.get(53)
  end

end