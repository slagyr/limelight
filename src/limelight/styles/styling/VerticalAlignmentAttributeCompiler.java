package limelight.styles.styling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;

public class VerticalAlignmentAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(String value)
  {
    String lowerCase = value.toLowerCase().trim();
    if("top".equals(lowerCase) || "center".equals(lowerCase) || "bottom".equals(lowerCase))
      return new SimpleVerticalAlignmentAttribute(lowerCase);
    else
      throw makeError(value);

  }
}
