package limelight.styles.attributes;

import limelight.styles.StyleAttribute;
import limelight.styles.abstrstyling.StyleValue;
import limelight.ui.model.ChangeablePanel;

public class FontStyleAttribute extends StyleAttribute
{
  public FontStyleAttribute()
  {
    super("Font Style", "font style", "plain");
  }

  @Override
  public void applyChange(ChangeablePanel panel, StyleValue value)
  {
    handleFontChange(panel);
  }
}
