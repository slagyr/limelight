#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

on_cast do
  text_area = Limelight::UI::Model::Inputs::TextAreaPanel.new
  panel.add(text_area)
  @text_area = text_area
end

attr_accessor :text_area #:nodoc:
