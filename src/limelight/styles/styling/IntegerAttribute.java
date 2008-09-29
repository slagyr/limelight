package limelight.styles.styling;

import limelight.styles.StyleAttribute;

public class IntegerAttribute implements StyleAttribute
{
  private int value;

  public IntegerAttribute(int value)
  {
    this.value = value;
  }

  public int getValue()
  {
    return value;
  }

  public String toString()
  {
    return "" + value;
  }

  public boolean equals(Object obj)
  {
    if(obj instanceof IntegerAttribute)
    {
      return value == ((IntegerAttribute)obj).value;
    }
    return false;
  }
}
