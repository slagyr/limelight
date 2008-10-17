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
  end
  image_area do
    logo :id => "logo", :players => "image", :image => "images/logo.png"
  end
end
