#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

sample {
  width 320
  height 320
  gradient :on
}

spinner {
  extends :sample
  background_color :green
  secondary_background_color :blue
  gradient_angle 0
  gradient_penetration 100
}

wave {
  extends :sample
  background_color :red
  secondary_background_color :yellow
  gradient_angle 0
  gradient_penetration 100
}

waves {
  extends :sample
  background_color "#F0F"
  secondary_background_color "#222"
  gradient_angle 90
  gradient_penetration 100
  cyclic_gradient :on
}

teaser_back {
  extends :sample
  gradient :off
  background_image "images/arch.jpg"
}

teaser {
  extends :sample
  background_color "#0000"
  secondary_background_color :black
  gradient_angle 45
  gradient_penetration 100	
}