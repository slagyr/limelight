package limelight.styles.styling;

import limelight.styles.StyleAttribute;

public class HorizontalAlignmentAttribute implements StyleAttribute
{
  private String alignment;

  public HorizontalAlignmentAttribute(String alignment)
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
    if(obj instanceof HorizontalAlignmentAttribute)
    {
      return alignment.equals(((HorizontalAlignmentAttribute)obj).alignment);  
    }
    return false;
  }
}
