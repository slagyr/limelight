package limelight.styles.styling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;

public class HorizontalAlignmentAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(String value)
  {
    String lowerCase = value.toLowerCase().trim();
    if("left".equals(lowerCase) || "center".equals(lowerCase) || "right".equals(lowerCase))
      return new SimpleHorizontalAlignmentAttribute(lowerCase);
    else
      throw makeError(value);

  }
}
