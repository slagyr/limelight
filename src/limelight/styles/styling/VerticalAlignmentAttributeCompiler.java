package limelight.styles.styling;

import limelight.styles.StyleAttributeCompiler;
import limelight.styles.StyleAttribute;

public class VerticalAlignmentAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(String value)
  {
    String lowerCase = value.toLowerCase().trim();
    if("top".equals(lowerCase) || "center".equals(lowerCase) || "bottom".equals(lowerCase))
      return new VerticalAlignmentAttribute(lowerCase);
    else
      throw makeError(value);

  }
}
