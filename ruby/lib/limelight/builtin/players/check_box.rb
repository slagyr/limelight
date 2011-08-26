#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

on_cast do
  check_box = Java::limelight.ui.model.inputs.CheckBoxPanel.new
  peer.add(check_box)
  @check_box = check_box
end

attr_accessor :check_box #:nodoc:

# Will place or remove a check mark in the check box.
#
def checked=(value)
  check_box.selected = value
end

# Returns true if the check box is checked.
#
def checked
  return check_box.selected?
end

alias :checked? :checked
