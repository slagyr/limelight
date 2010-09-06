#- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

__ :name => "sandbox"
__install "header.rb"
arena do
  toolbar do
    tool do
      label :text => "Line\n(press and drag to draw)"
      selector :players => "radio_button", :group => "tools", :id => "line", :on_button_pushed => "scene.find('sketchpad').activate(:line)"
    end
    tool do
      label :text => "Square\n(click to draw)"
      selector :players => "radio_button", :group => "tools", :id => "square", :on_button_pushed => "scene.find('sketchpad').activate(:square)"
    end
    tool do
      label :text => "Circle\n(click to draw)"
      selector :players => "radio_button", :group => "tools", :id => "circle", :on_button_pushed => "scene.find('sketchpad').activate(:circle)"
    end
    tool do
      button :text => "Clear", :on_button_pushed => "scene.find('sketchpad').clear"
    end
  end
  sketchpad :id => "sketchpad"
end