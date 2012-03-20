#- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

__install "header.rb"
arena do

  control_panel do
    setting do
      label :text => "Child Width:"
      input :players => "text_box", :text => "50", :id => "child_width"
    end
    setting do
      label :text => "Child Height:"
      input :players => "text_box", :text => "50", :id => "child_height"
    end
    setting do
      label :text => "# Children:"
      input :players => "text_box", :text => "100", :id => "child_count"
    end
    actions do
      action :players => "button", :text => "Add Children", :on_button_pushed => "scene.add_children"
      action :players => "button", :text => "Clear Children", :on_button_pushed => "scene.clear_children"
      action :players => "button", :text => "Re-Add Children", :on_button_pushed => "scene.readd_children"
    end
  end

  container :id => "container"
end