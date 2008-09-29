package limelight.styles.styling;

import limelight.styles.StyleAttributeCompiler;
import limelight.styles.StyleAttribute;

public class HorizontalAlignmentAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(String value)
  {
    String lowerCase = value.toLowerCase().trim();
    if("left".equals(lowerCase) || "center".equals(lowerCase) || "right".equals(lowerCase))
      return new HorizontalAlignmentAttribute(lowerCase);
    else
      throw makeError(value);

  }
}
