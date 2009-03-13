//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.HorizontalAlignmentAttribute;
import limelight.util.Aligner;
import limelight.LimelightError;

public class SimpleHorizontalAlignmentAttribute implements HorizontalAlignmentAttribute
{
  private final Aligner.HorizontalAligner alignment;

  public SimpleHorizontalAlignmentAttribute(Aligner.HorizontalAligner alignment)
  {
    this.alignment = alignment;
  }

  public Aligner.HorizontalAligner getAlignment()
  {
    return alignment;
  }

  public String toString()
  {
    if(alignment == Aligner.LEFT)
      return "left";
    else if(alignment == Aligner.HORIZONTAL_CENTER)
      return "center";
    else if(alignment == Aligner.RIGHT)
      return "right";
    else
      throw new LimelightError("Unknown Horizontal Alignment: " + alignment);
  }

  public boolean equals(Object obj)
  {
    if(obj instanceof SimpleHorizontalAlignmentAttribute)
    {
      return alignment.equals(((SimpleHorizontalAlignmentAttribute)obj).alignment);
    }
    return false;
  }
}
