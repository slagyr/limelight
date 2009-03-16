#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

__ :name => "inspector"
tool_bar do
  tool :text => "New", :on_mouse_clicked => "scene.new"
  tool :text => "Open", :on_mouse_clicked => "scene.open"
end
prop_tree :id => "prop_tree" do

end
style_table :id => 'style_table'  do
  Limelight::Styles::Style::STYLE_LIST.each do |style|
    style_row do
      style_name :text => style.name
      style_value :players => "text_box", :descriptor => style
    end
  end
end
