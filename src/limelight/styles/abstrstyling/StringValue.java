//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.abstrstyling;

import limelight.util.Util;

public class StringValue implements StyleValue
{
  private final String stringValue;

  public StringValue(Object value)
  {
    this.stringValue = value.toString();
  }

  public String toString()
  {
    return stringValue;
  }

  public boolean equals(Object obj)
  {
    if(obj instanceof StringValue)
      return Util.equal(stringValue, ((StringValue) obj).stringValue);
    else
      return false;
  }

  public String getValue()
  {
    return stringValue;
  }
}
