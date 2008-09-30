package limelight.styles.styling;

import limelight.ui.painting.ImageFillStrategy;
import limelight.ui.painting.StaticImageFillStrategy;
import limelight.ui.painting.RepeatingImageFillStrategy;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.abstrstyling.FillStrategyAttribute;

public class SimpleFillStrategyAttribute implements FillStrategyAttribute
{
  private ImageFillStrategy strategy;
  private String name;

  public SimpleFillStrategyAttribute(String name)
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
    if(obj instanceof SimpleFillStrategyAttribute)
    {
      return name.equals(((SimpleFillStrategyAttribute)obj).name);
    }
    return false;
  }
}
