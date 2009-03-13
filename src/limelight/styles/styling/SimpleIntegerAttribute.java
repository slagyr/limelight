//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.IntegerAttribute;

public class SimpleIntegerAttribute implements IntegerAttribute
{
  private final int value;

  public SimpleIntegerAttribute(int value)
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
    if(obj instanceof SimpleIntegerAttribute)
    {
      return value == ((SimpleIntegerAttribute)obj).value;
    }
    return false;
  }
}
