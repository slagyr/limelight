#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

on_cast do
  text_area = Java::limelight.ui.model.inputs.TextAreaPanel.new
  peer.add(text_area)
  @text_area = text_area
end

attr_accessor :text_area #:nodoc:
