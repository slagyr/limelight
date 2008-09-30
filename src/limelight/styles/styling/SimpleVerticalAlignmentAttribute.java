package limelight.styles.styling;

import limelight.styles.abstrstyling.VerticalAlignmentAttribute;

public class SimpleVerticalAlignmentAttribute implements VerticalAlignmentAttribute
{
  private String alignment;

  public SimpleVerticalAlignmentAttribute(String alignment)
  {
    this.alignment = alignment;
  }

  public String getAlignment()
  {
    return alignment;
  }

  public String toString()
  {
    return alignment;
  }

  public boolean equals(Object obj)
  {
    if(obj instanceof SimpleVerticalAlignmentAttribute)
    {
      return alignment.equals(((SimpleVerticalAlignmentAttribute)obj).alignment);
    }
    return false;
  }
}
