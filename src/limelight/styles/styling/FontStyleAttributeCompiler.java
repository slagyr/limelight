package limelight.styles.styling;

import limelight.styles.StyleAttributeCompiler;
import limelight.styles.StyleAttribute;

public class FontStyleAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(String value)
  {
    String lowerCase = value.toLowerCase().trim();

    if("plain".equals(lowerCase))
      return new FontStyleAttribute("plain");
    else
    {
      String[] tokens = lowerCase.split(" ");
      for(String token : tokens)
      {
        if(!"bold".equals(token) && !"italic".equals(token))
          throw makeError(value);
      }
      return new FontStyleAttribute(lowerCase);
    }

  }
}
