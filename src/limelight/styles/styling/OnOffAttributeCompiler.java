package limelight.styles.styling;

import limelight.styles.StyleAttributeCompiler;
import limelight.styles.StyleAttribute;

public class OnOffAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(String value)
  {
    String lowerCaseValue = value.toLowerCase();
    if("on".equals(lowerCaseValue))
      return new OnOffAttribute(true);
    else if("off".equals(lowerCaseValue))
      return new OnOffAttribute(false);
    else
      throw makeError(value);
  }
}
