//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.LimelightError;
import limelight.styles.abstrstyling.VerticalAlignmentAttribute;
import limelight.util.Aligner;

public class SimpleVerticalAlignmentAttribute implements VerticalAlignmentAttribute
{
  private final Aligner.VerticalAligner alignment;

  public SimpleVerticalAlignmentAttribute(Aligner.VerticalAligner alignment)
  {
    this.alignment = alignment;
  }

  public Aligner.VerticalAligner getAlignment()
  {
    return alignment;
  }

  public String toString()
  {
    if(alignment == Aligner.TOP)
      return "top";
    else if(alignment == Aligner.VERTICAL_CENTER)
      return "center";
    else if(alignment == Aligner.BOTTOM)
      return "bottom";
    else
      throw new LimelightError("Unknown Vertical Alignment: " + alignment);
  }

  public boolean equals(Object obj)
  {
    if(obj instanceof SimpleVerticalAlignmentAttribute)
    {
      return alignment.equals(((SimpleVerticalAlignmentAttribute) obj).alignment);
    }
    return false;
  }
}
