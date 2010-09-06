#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

on_cast do
  check_box = Limelight::UI::Model::Inputs::CheckBoxPanel.new
  panel.add(check_box)
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
