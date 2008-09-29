package limelight.styles.styling;

import limelight.styles.StyleAttribute;

public class FontStyleAttribute implements StyleAttribute
{
  private boolean bold;
  private boolean italic;

  public FontStyleAttribute(String styling)
  {
    String[] tokens = styling.split(" ");
    for(String token : tokens)
    {
      if("bold".equals(token))
        bold = true;
      else if("italic".equals(token))
        italic = true;
    }
  }

  public boolean isBold()
  {
    return bold;
  }

  public boolean isItalic()
  {
    return italic;
  }

  public String toString()
  {
    if(!bold && !italic)
      return "plain";
    else
    {
      StringBuffer buffer = new StringBuffer();
      if(bold)
        buffer.append("bold ");
      if(italic)
        buffer.append("italic ");
      return buffer.toString().trim();
    }
  }

  public boolean equals(Object obj)
  {
    if(obj instanceof FontStyleAttribute)
    {
      FontStyleAttribute other = (FontStyleAttribute)obj;
      if(bold != other.bold)
        return false;
      if(italic != other.italic)
        return false;
      
      return true;
    }
    return false;
  }
}
