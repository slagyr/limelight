package limelight.styles.attributes;

import limelight.styles.StyleAttribute;
import limelight.styles.abstrstyling.StyleValue;
import limelight.ui.model.ChangeablePanel;

public class FontSizeAttribute extends StyleAttribute
{
  public FontSizeAttribute()
  {
    super("Font Size", "integer", "12");
  }

  @Override
  public void applyChange(ChangeablePanel panel, StyleValue value)
  {
    handleFontChange(panel);
  }
}
