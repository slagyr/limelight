package limelight.styles.styling;

import limelight.styles.StyleAttribute;

import java.awt.*;

public class ColorAttribute implements StyleAttribute
{
  private Color color;

  public ColorAttribute(Color color)
  {
    this.color = color;
  }

  public Color getColor()
  {
    return color;
  }

  public String toString()
  {
    String r = Integer.toHexString(color.getRed());
    String g = Integer.toHexString(color.getGreen());
    String b = Integer.toHexString(color.getBlue());
    String a = Integer.toHexString(color.getAlpha());
    StringBuffer buffer = new StringBuffer("#");
    if(r.length() == 1)
      buffer.append("0");
    buffer.append(r);
    if(g.length() == 1)
      buffer.append("0");
    buffer.append(g);
    if(b.length() == 1)
      buffer.append("0");
    buffer.append(b);
    if(a.length() == 1)
      buffer.append("0");
    buffer.append(a);
    return buffer.toString();
  }

  public boolean equals(Object obj)
  {
    if(obj instanceof ColorAttribute)
    {
      return color.equals( ((ColorAttribute)obj).color );  
    }
    return false;
  }
}
