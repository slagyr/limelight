__ :name => "sandbox"
__install "header.rb"
arena :vertical_alignment => :center do
  sample do
    control_panel do
      setting do
        label :text => "Width:"
        input :players => "text_box", :text => "auto", :id => "text_box_width_input", :on_focus_lost => "scene.find('textbox').style.width = text"
      end
      setting do
        label :text => "Height:"
        input :players => "text_box", :text => "auto", :id => "text_box_height_input", :on_focus_lost => "scene.find('textbox').style.height = text"
      end
      setting do
        label :text => "Font Face:"
        input :players => "text_box", :text => "arial", :id => "text_box_font_face", :on_focus_lost => "scene.find('textbox').style.font_face = text"
      end
      setting do
        label :text => "Font Size:"
        input :players => "text_box", :text => "12", :id => "text_box_font_size", :on_focus_lost => "scene.find('textbox').style.font_size = text"
      end
      setting do
        label :text => "Text Color:"
        input :players => "text_box", :text => "black", :id => "text_box_text_color", :on_focus_lost => "scene.find('textbox').style.text_color = text"
      end
      setting do
        label :text => "Background Color:"
        input :players => "text_box", :text => "white", :id => "text_box_background_color", :on_focus_lost => "scene.find('textbox').style.background_color = text"
      end
    end
    text_box_area do
      the_text_box :id => "textbox", :players => "text_box2"
    end
  end

  sample do
    control_panel do
      setting do
        label :text => "Width:"
        input :players => "text_box", :text => "auto", :id => "text_area_width_input", :on_focus_lost => "scene.find('textarea').style.width = text"
      end
      setting do
        label :text => "Height:"
        input :players => "text_box", :text => "auto", :id => "text_area_height_input", :on_focus_lost => "scene.find('textarea').style.height = text"
      end
      setting do
        label :text => "Font Face:"
        input :players => "text_box", :text => "arial", :id => "text_area_font_face", :on_focus_lost => "scene.find('textarea').style.font_face = text"
      end
      setting do
        label :text => "Font Size:"
        input :players => "text_box", :text => "12", :id => "text_area_font_size", :on_focus_lost => "scene.find('textarea').style.font_size = text"
      end
      setting do
        label :text => "Text Color:"
        input :players => "text_box", :text => "black", :id => "text_area_text_color", :on_focus_lost => "scene.find('textarea').style.text_color = text"
      end
      setting do
        label :text => "Background Color:"
        input :players => "text_box", :text => "white", :id => "text_area_background_color", :on_focus_lost => "scene.find('textarea').style.background_color = text"
      end
    end
    text_box_area do
      the_text_area :id => "textarea", :players => "text_area2", :text => "blah"
    end
  end
end
