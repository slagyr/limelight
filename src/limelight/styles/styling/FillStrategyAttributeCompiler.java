package limelight.styles.styling;

import limelight.styles.StyleAttributeCompiler;
import limelight.styles.StyleAttribute;

public class FillStrategyAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(String value)
  {
    String name = value.toLowerCase();
    if("static".equals(name) || "repeat".equals(name))
      return new FillStrategyAttribute(name);
    else
      throw makeError(value);
  }
}
