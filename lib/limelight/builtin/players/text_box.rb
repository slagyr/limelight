#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

on_cast do
  text_box = Limelight::UI::Model::Inputs::TextBoxPanel.new
  panel.add(text_box)
  @text_box = text_box
end

attr_accessor :text_box #:nodoc:
