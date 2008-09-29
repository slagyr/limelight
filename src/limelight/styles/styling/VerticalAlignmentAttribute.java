package limelight.styles.styling;

import limelight.styles.StyleAttribute;

public class VerticalAlignmentAttribute implements StyleAttribute
{
  private String alignment;

  public VerticalAlignmentAttribute(String alignment)
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
    if(obj instanceof VerticalAlignmentAttribute)
    {
      return alignment.equals(((VerticalAlignmentAttribute)obj).alignment);
    }
    return false;
  }
}
