#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

__ :name => "sandbox"
__install "header.rb"
arena :vertical_alignment => :center do
  control_panel do
    setting do
      label :text => "Width:"
      input :players => "text_box", :text => "auto", :on_focus_lost => "scene.find('logo').style.width = text"
    end
    setting do
      label :text => "Height:"
      input :players => "text_box", :text => "auto", :on_focus_lost => "scene.find('logo').style.height = text"
    end
    setting do
      label :text => "Scaled:", :width => "100%"
      input :players => "check_box", :checked => true, :on_button_pressed => "scene.find('logo').scaled = checked?"
    end
    setting do
      label :text => "Rotation:"
      input :players => "text_box", :text => "0", :on_focus_lost => "scene.find('logo').rotation = text.to_f"
    end
    setting do
      label :text => "Horizontal Align:"
      input :players => "combo_box", :choices => %w{left center right}, :on_value_changed => "scene.find('logo').style.horizontal_alignment = text"
    end
    setting do
      label :text => "Vertical Align:"
      input :players => "combo_box", :choices => %w{top center bottom}, :on_value_changed => "scene.find('logo').style.vertical_alignment = text"
    end
  end
  image_area do
    logo :id => "logo", :players => "image", :image => "images/logo.png"
  end
end
