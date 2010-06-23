setting do
  label :text => "Width:"
  input :players => "text_box", :text => "auto", :id => "#{@input_id}_width", :on_focus_lost => "scene.find('#{@input_id}').style.width = text"
end
setting do
  label :text => "Height:"
  input :players => "text_box", :text => "auto", :id => "#{@input_id}_height", :on_focus_lost => "scene.find('#{@input_id}').style.height = text"
end
setting do
  label :text => "Font Face:"
  input :players => "text_box", :text => "arial", :id => "#{@input_id}_font_face", :on_focus_lost => "scene.find('#{@input_id}').style.font_face = text"
end
setting do
  label :text => "Font Size:"
  input :players => "text_box", :text => "12", :id => "#{@input_id}_font_size", :on_focus_lost => "scene.find('#{@input_id}').style.font_size = text"
end
setting do
  label :text => "Text Color:"
  input :players => "text_box", :text => "black", :id => "#{@input_id}_text_color", :on_focus_lost => "scene.find('#{@input_id}').style.text_color = text"
end
setting do
  label :text => "Background Color:"
  input :players => "text_box", :text => "white", :id => "#{@input_id}_background_color", :on_focus_lost => "scene.find('#{@input_id}').style.background_color = text"
end
setting do
  label :text => "Border Color:"
  input :players => "text_box", :text => "transparent", :id => "#{@input_id}_border_color", :on_focus_lost => "scene.find('#{@input_id}').style.border_color = text"
end
setting do
  label :text => "Border Width:"
  input :players => "text_box", :text => "4", :id => "#{@input_id}_border_width", :on_focus_lost => "scene.find('#{@input_id}').style.border_width = text"
end
setting do
  label :text => "Vertical Align:"
  input :players => "text_box", :text => "center", :id => "#{@input_id}_vertical_alignment", :on_focus_lost => "scene.find('#{@input_id}').style.vertical_alignment = text"
end
setting do
  label :text => "Horizontal Align:"
  input :players => "text_box", :text => "left", :id => "#{@input_id}_horizontal_alignment", :on_focus_lost => "scene.find('#{@input_id}').style.horizontal_alignment = text"
end
setting do
  label :text => "Padding:"
  input :players => "text_box", :text => "2", :id => "#{@input_id}_padding", :on_focus_lost => "scene.find('#{@input_id}').style.padding = text"
end