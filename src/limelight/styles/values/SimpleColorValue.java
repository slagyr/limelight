//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.styles.abstrstyling.ColorValue;

import java.awt.*;

public class SimpleColorValue implements ColorValue
{
  private final Color color;

  public SimpleColorValue(Color color)
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
    if(obj instanceof SimpleColorValue)
    {
      return color.equals( ((SimpleColorValue)obj).color );
    }
    return false;
  }
}
