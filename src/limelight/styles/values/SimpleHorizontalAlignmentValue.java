//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.styles.abstrstyling.HorizontalAlignmentValue;
import limelight.styles.HorizontalAlignment;
import limelight.LimelightError;

import java.awt.*;

public class SimpleHorizontalAlignmentValue implements HorizontalAlignmentValue
{
  private final HorizontalAlignment alignment;

  public SimpleHorizontalAlignmentValue(HorizontalAlignment alignment)
  {
    this.alignment = alignment;
  }

  public HorizontalAlignment getAlignment()
  {
    return alignment;
  }

  public String toString()
  {
    if(alignment == HorizontalAlignment.LEFT)
      return "left";
    else if(alignment == HorizontalAlignment.CENTER)
      return "center";
    else if(alignment == HorizontalAlignment.RIGHT)
      return "right";
    else
      throw new LimelightError("Unknown Horizontal Alignment: " + alignment);
  }

  public boolean equals(Object obj)
  {
    if(obj instanceof SimpleHorizontalAlignmentValue)
    {
      return alignment.equals(((SimpleHorizontalAlignmentValue)obj).alignment);
    }
    return false;
  }

  public int getX(int consumed, Rectangle area)
  {
    if(alignment == HorizontalAlignment.LEFT)
      return area.x;
    else if(alignment == HorizontalAlignment.CENTER)
      return area.x + (area.width - (int) (consumed + 0.5)) / 2;
    else
      return area.x + (area.width - (int) (consumed + 0.5));

  }
}
