package limelight.styles.styling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;

public class FillStrategyAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(Object value)
  {
    String name = value.toString().toLowerCase();
    if("static".equals(name) || "repeat".equals(name))
      return new SimpleFillStrategyAttribute(name);
    else
      throw makeError(value);
  }
}
