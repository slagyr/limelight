#- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

__ :name => "sandbox"
__install "header.rb"
arena :vertical_alignment => :center do
  control_panel do
    setting do
      label :text => "Width:"
      input :players => "text_box", :text => "auto", :id => "width_input", :on_focus_lost => "scene.find('logo').style.width = text"
    end
    setting do
      label :text => "Height:"
      input :players => "text_box", :text => "auto", :id => "height_input", :on_focus_lost => "scene.find('logo').style.height = text"
    end
    setting do
      label :text => "Scaled:", :width => "100%"
      input :players => "check_box", :checked => true, :id => "scaled_checkbox", :on_button_pushed => "scene.find('logo').scaled = checked?"
    end
    setting do
      label :text => "Rotation:"
      input :players => "text_box", :text => "0", :id => "rotation_input", :on_focus_lost => "scene.find('logo').rotation = text.to_f"
    end
    setting do
      label :text => "Horizontal Align:"
      input :players => "combo_box", :choices => %w{left center right}, :id => "horizontal_align_input", :on_value_changed => "scene.find('logo').style.horizontal_alignment = text"
    end
    setting do
      label :text => "Vertical Align:"
      input :players => "combo_box", :choices => %w{top center bottom}, :id => "vertical_align_input", :on_value_changed => "scene.find('logo').style.vertical_alignment = text"
    end
  end
  image_area do
    logo :id => "logo", :players => "image", :image => "images/logo.png"
  end
end
