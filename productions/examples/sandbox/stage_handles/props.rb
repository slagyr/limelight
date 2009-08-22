#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

__ :styles => "sandbox"
__install "header.rb"
arena do
  mover :players => "stage_mover", :text => "Mover"
  spacer
  sizer_links do
    [:top, :center, :bottom].each do |y|
      [:left, :center, :right].each do |x|
        sizer_link :text => "#{x}, #{y}", :on_mouse_clicked => "scene.find('sizer').go(:#{x}, :#{y})"
      end
    end
  end
end
sizer :id => "sizer", :players => "stage_sizer", :text => "Sizer"