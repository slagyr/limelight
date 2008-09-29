package limelight.styles.styling;

import limelight.styles.StyleAttributeCompiler;
import limelight.styles.StyleAttribute;

public class StringAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(String value)
  {
     return new StringAttribute(value);
  }
}
