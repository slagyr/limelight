package limelight.styles.attributes;

import limelight.styles.StyleAttribute;
import limelight.styles.abstrstyling.StyleValue;
import limelight.ui.model.ChangeablePanel;

public class TransparencyAttribute extends StyleAttribute
{
  public TransparencyAttribute()
  {
    super("Transparency", "percentage", "0%");
  }

  @Override
  public void applyChange(ChangeablePanel panel, StyleValue value)
  {
    panel.markAsDirty();
  }
}
