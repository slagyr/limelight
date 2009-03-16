//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.OnOffAttribute;

public class SimpleOnOffAttribute implements OnOffAttribute
{
  private final boolean isOn;

  public SimpleOnOffAttribute(boolean isOn)
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
    if(obj instanceof SimpleOnOffAttribute)
    {
      return isOn == ((SimpleOnOffAttribute)obj).isOn;
    }
    return false;
  }
}
