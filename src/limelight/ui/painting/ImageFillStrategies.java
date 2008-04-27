package limelight.ui.painting;

public class ImageFillStrategies
{
  public static ImageFillStrategy get(String name)
  {
    if ("static".equals(name))
      return new StaticImageFillStrategy();
    else if ("repeat".equals(name))
      return new RepeatingImageFillStrategy();
    else
      return new RepeatingImageFillStrategy();
  }
}
