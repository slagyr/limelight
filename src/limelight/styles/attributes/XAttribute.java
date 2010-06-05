package limelight.styles.attributes;

import limelight.styles.StyleAttribute;
import limelight.styles.abstrstyling.StyleValue;
import limelight.ui.model.ChangeablePanel;
import limelight.ui.model.FloaterLayout;

public class XAttribute extends StyleAttribute
{
  public XAttribute()
  {
    super("X", "x-coordinate", "0");
  }

  @Override
  public void applyChange(ChangeablePanel panel, StyleValue value)
  {
    panel.markAsNeedingLayout(FloaterLayout.instance);
  }
}
