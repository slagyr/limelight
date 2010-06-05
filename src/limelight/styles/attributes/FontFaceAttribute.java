package limelight.styles.attributes;

import limelight.styles.StyleAttribute;
import limelight.styles.abstrstyling.StyleValue;
import limelight.ui.model.ChangeablePanel;

public class FontFaceAttribute extends StyleAttribute
{
  public FontFaceAttribute()
  {
    super("Font Face", "string", "Arial");
  }

  @Override
  public void applyChange(ChangeablePanel panel, StyleValue value)
  {
    handleFontChange(panel);
  }
}
