package limelight.styles.styling;

import limelight.styles.abstrstyling.OnOffAttribute;

public class SimpleOnOffAttribute implements OnOffAttribute
{
  private boolean isOn;

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
