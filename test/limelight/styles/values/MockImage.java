//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

public class MockImage extends Image
{
  public int height;
  public int width;

  public MockImage(int width, int height)
  {
    this.width = width;
    this.height = height;
  }

  public int getWidth(ImageObserver observer)
  {
    return width;
  }

  public int getHeight(ImageObserver observer)
  {
    return height;
  }

  public ImageProducer getSource()
  {
    return null;
  }

  public Graphics getGraphics()
  {
    return null;
  }

  public Object getProperty(String name, ImageObserver observer)
  {
    return null;
  }

  public void flush()
  {
  }
}
