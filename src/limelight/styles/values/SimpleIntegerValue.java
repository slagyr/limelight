//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.styles.abstrstyling.IntegerValue;

public class SimpleIntegerValue implements IntegerValue
{
  private final int value;

  public SimpleIntegerValue(int value)
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
    if(obj instanceof SimpleIntegerValue)
    {
      return value == ((SimpleIntegerValue)obj).value;
    }
    return false;
  }
}
