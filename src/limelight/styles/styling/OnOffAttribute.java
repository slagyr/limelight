package limelight.styles.styling;

import limelight.styles.StyleAttribute;

public class OnOffAttribute implements StyleAttribute
{
  private boolean isOn;

  public OnOffAttribute(boolean isOn)
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
    if(obj instanceof OnOffAttribute)
    {
      return isOn == ((OnOffAttribute)obj).isOn;  
    }
    return false;
  }
}
