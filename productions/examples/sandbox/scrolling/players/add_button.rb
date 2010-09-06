#- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.


on_button_pushed do

  table = scene.find("#{id.split("_")[0]}_table")
  table.children.each do |row|
    if row.children.length < 10
      row.add(Limelight::Prop.new(:name => "cell", :text => "CELL", :background_color => "orange", :secondary_background_color => "gray", :gradient_angle => "45", :gradient => "on"))
    end
  end

end