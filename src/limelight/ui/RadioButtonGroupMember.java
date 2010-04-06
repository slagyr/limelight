//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

public interface RadioButtonGroupMember
{
  boolean isSelected();

  void setGroup(RadioButtonGroup radioButtonGroup);

  void setSelected(boolean value);
}
