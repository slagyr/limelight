#- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

hamlet = <<END
To be, or not to be.  That is the question.  Whether tis nobler in the mind to suffer the slings and arrows of time,
or to take arms against a sea of troubles and by opposing, end them.
END

__ :name => "sandbox"
__install "header.rb"
arena :vertical_alignment => :center do
  control_panel do
    setting do
      label :text => "Margin:"
      input :players => "text_box", :id => "margin_input", :text => "10%", :on_focus_lost => "puts 'Hiho!'; scene.apply(:margin, text)"
    end
    setting do
      label :text => "Border Width:"
      input :players => "text_box", :id => "border_input", :text => "10%", :on_focus_lost => "scene.apply(:border_width, text)"
    end
    setting do
      label :text => "Padding:"
      input :players => "text_box", :id => "padding_input", :text => "10%", :on_focus_lost => "scene.apply(:padding, text)"
    end
    setting do
      label :text => "Rounded Corner Radius:"
      input :players => "text_box", :id => "corner_input", :text => "10%", :on_focus_lost => "scene.apply(:rounded_corner_radius, text)"
    end
  end
  image_area do
    box do
      subject :id => "subject", :text => hamlet
    end 
  end
end
