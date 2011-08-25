//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

import java.awt.image.VolatileImage;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.*;

public class MockVolatileImage extends VolatileImage
{
  public BufferedImage getSnapshot()
  {
    return null;
  }

  public int getWidth()
  {
    return 0;
  }

  public int getHeight()
  {
    return 0;
  }

  public Graphics2D createGraphics()
  {
    return null;
  }

  public int validate(GraphicsConfiguration gc)
  {
    return 0;
  }

  public boolean contentsLost()
  {
    return false;
  }

  public ImageCapabilities getCapabilities()
  {
    return null;
  }

  public int getWidth(ImageObserver observer)
  {
    return 0;
  }

  public int getHeight(ImageObserver observer)
  {
    return 0;
  }

  public Object getProperty(String name, ImageObserver observer)
  {
    return null;
  }
}
