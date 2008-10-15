package limelight.styles.styling;

import limelight.styles.abstrstyling.PercentageAttribute;

public class SimplePercentageAttribute implements PercentageAttribute
{
  private final int percentage;

  public SimplePercentageAttribute(int percentage)
  {
    this.percentage = percentage;
  }

  public int getPercentage()
  {
    return percentage;
  }

  public String toString()
  {
    return percentage + "%"; 
  }

  public boolean equals(Object obj)
  {
    if(obj instanceof SimplePercentageAttribute)
    {
      return percentage == ((SimplePercentageAttribute)obj).percentage;
    }
    return false;
  }
}
