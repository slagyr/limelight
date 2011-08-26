//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui;

public interface RadioButtonGroupMember
{
  boolean isSelected();

  void setGroup(RadioButtonGroup radioButtonGroup);

  void setSelected(boolean value);
}
