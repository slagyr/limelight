#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

on_cast do
  text_box = Java::limelight.ui.model.inputs.TextBoxPanel.new
  peer.add(text_box)
  @text_box = text_box
end

attr_accessor :text_box #:nodoc:
