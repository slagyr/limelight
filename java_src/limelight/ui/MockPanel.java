package limelight.ui;

import java.awt.image.BufferedImage;
import java.awt.*;

class MockPanel extends Panel
{
  public BufferedImage buffer;

  public BufferedImage getBuffer()
  {
    return buffer;
  }

  public void snapToSize()
  {
  }

  public void paintOn(Graphics2D graphics)
  {
  }
}
