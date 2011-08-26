#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

on_cast do
  combo_box = Java::limelight.ui.model.inputs.ComboBoxPanel.new
  @combo_box = combo_box
  peer.add(combo_box)
  clear
end

attr_accessor :combo_box

def clear #:nodoc:
  @combo_box.clear
end

# Sets the choices listed in the combo_box.  The value parameter should an Array or a String that
# can be evalulated into an Array.  All choices in a combo_box are strings.
#
# combo_box.choices = ["one", "two", "three"]
# combo_box.choices = "['one', 'two', 'three']"
#
def choices=(value)
  value = eval(value) if value.is_a? String
  raise "Invalid choices type: #{value.class}.  Choices have to be an array." if !value.is_a?(Array)
  value.each { |option| @combo_box.add_option(option) }
end

alias :options= :choices=

# returns an array of the choices in this combo box.
#
def choices
  return @combo_box.options
end

alias :options :choices

# Sets the value of the combo box. The value provided should be one choices in the combo box.
#
def value=(text)
  @combo_box.text = text
end

# Returns the value of the currently selected choice.
#
def value
  return @combo_box.text
end

