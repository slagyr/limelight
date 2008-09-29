package limelight.styles.styling;

import limelight.styles.StyleAttribute;

public class PercentageAttribute implements StyleAttribute
{
  private int percentage;

  public PercentageAttribute(int percentage)
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
    if(obj instanceof PercentageAttribute)
    {
      return percentage == ((PercentageAttribute)obj).percentage;  
    }
    return false;
  }
}
