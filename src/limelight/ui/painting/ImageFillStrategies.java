//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

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
