package limelight.styles.styling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.abstrstyling.StringAttribute;

public class StringAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(Object value)
  {
     return new StringAttribute(value);
  }
}
