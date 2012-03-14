//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.images;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

public class Images
{
  public static BufferedImage load(String path)
  {
    try
    {
      final URL url = resolve(path);
      return ImageIO.read(url);
    }
    catch(Exception e)
    {
      System.err.println("Failed to load image: " + path);
      e.printStackTrace();
      return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    }
  }

  public static URL resolve(String path)
  {
    return Images.class.getClassLoader().getResource("limelight/ui/images/" + path);
  }
}
