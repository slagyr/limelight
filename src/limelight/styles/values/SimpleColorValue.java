//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.styles.abstrstyling.ColorValue;
import limelight.util.Colors;

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
    return Colors.toString(color);
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
