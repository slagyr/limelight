//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.styles.abstrstyling.OnOffValue;

public class SimpleOnOffValue implements OnOffValue
{
  private final boolean isOn;

  public SimpleOnOffValue(boolean isOn)
  {
    this.isOn = isOn;
  }

  public boolean isOn()
  {
    return isOn;
  }

  public boolean isOff()
  {
    return !isOn;
  }

  public String toString()
  {
    return isOn ? "on" : "off";
  }

  public boolean equals(Object obj)
  {
    if(obj instanceof SimpleOnOffValue)
    {
      return isOn == ((SimpleOnOffValue)obj).isOn;
    }
    return false;
  }
}
