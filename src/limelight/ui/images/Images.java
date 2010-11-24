//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.images;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Images
{
  public static BufferedImage load(String path)
  {
    try
    {
      return ImageIO.read(resolve(path));
    }
    catch(IOException e)
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
