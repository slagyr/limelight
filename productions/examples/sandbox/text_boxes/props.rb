__ :name => "sandbox"
__install "header.rb"
arena :vertical_alignment => :center do
  sample do
    control_panel do
      __install "text_boxes/settings.rb", :input_id => "textbox"
      setting do
        label :text => "Password:"
        input :players => "check_box", :id => "text_box_password", :on_value_changed => "scene.find('textbox').backstage['text-box'].password = backstage['check-box'].checked?"
      end
    end
    text_box_area do
      the_text_box :id => "textbox", :players => "text_box"
    end
  end

  sample do
    control_panel do
      __install "text_boxes/settings.rb", :input_id => "textarea"
    end
    text_box_area do
      the_text_area :id => "textarea", :players => "text_area", :text => "blah"
    end
  end
end
