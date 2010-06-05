package limelight.styles.attributes;

import limelight.styles.StyleAttribute;
import limelight.styles.abstrstyling.StyleValue;
import limelight.ui.model.ChangeablePanel;

public class BottomPaddingAttribute extends StyleAttribute
{
  public BottomPaddingAttribute()
  {
    super("Bottom Padding", "pixels", "0");
  }

  @Override
  public void applyChange(ChangeablePanel panel, StyleValue value)
  {
    handleInsetChange(panel);
  }
}
