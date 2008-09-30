package limelight.styles.styling;

import limelight.styles.abstrstyling.HorizontalAlignmentAttribute;

public class SimpleHorizontalAlignmentAttribute implements HorizontalAlignmentAttribute
{
  private String alignment;

  public SimpleHorizontalAlignmentAttribute(String alignment)
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
    if(obj instanceof SimpleHorizontalAlignmentAttribute)
    {
      return alignment.equals(((SimpleHorizontalAlignmentAttribute)obj).alignment);
    }
    return false;
  }
}
