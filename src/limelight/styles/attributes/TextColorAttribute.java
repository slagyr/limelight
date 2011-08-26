//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.attributes;

import limelight.styles.StyleAttribute;
import limelight.styles.abstrstyling.StyleValue;
import limelight.ui.model.ChangeablePanel;

public class TextColorAttribute extends StyleAttribute
{
  public TextColorAttribute()
  {
    super("Text Color", "color", "#000000ff");
  }

  @Override
  public void applyChange(ChangeablePanel panel, StyleValue value)
  {
    panel.getTextAccessor().markAsDirty();
  }
}
