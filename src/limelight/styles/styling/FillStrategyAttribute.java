package limelight.styles.styling;

import limelight.ui.painting.ImageFillStrategy;
import limelight.ui.painting.StaticImageFillStrategy;
import limelight.ui.painting.RepeatingImageFillStrategy;
import limelight.styles.StyleAttribute;

public class FillStrategyAttribute implements StyleAttribute
{
  private ImageFillStrategy strategy;
  private String name;

  public FillStrategyAttribute(String name)
  {
    this.name = name;
    if("static".equals(name))
      strategy = new StaticImageFillStrategy();
    else if("repeat".equals(name))
      strategy = new RepeatingImageFillStrategy();

  }

  public ImageFillStrategy getStrategy()
  {
    return strategy;
  }

  public String getName()
  {
    return name;
  }

  public String toString()
  {
    return name;
  }

  public boolean equals(Object obj)
  {
    if(obj instanceof FillStrategyAttribute)
    {
      return name.equals(((FillStrategyAttribute)obj).name);  
    }
    return false;
  }
}
