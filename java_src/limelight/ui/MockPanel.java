package limelight.ui;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.MouseEvent;

class MockPanel extends Panel
{
  public BufferedImage buffer;
  public int snapToWidth;
  public int snapToHeight;
  public boolean wasLaidOut;
  public boolean shouldUseBuffer;
  public Graphics2D paintedOnGraphics;

  public BufferedImage getBuffer()
  {
    return buffer;
  }

  public void prepForSnap(int width, int height)
  {
    snapToWidth = width;
    snapToHeight = height;
  }

  public void paintOn(Graphics2D graphics)
  {
    paintedOnGraphics = graphics;
  }

  public boolean usesBuffer()
  {
    return shouldUseBuffer;
  }

  public void doLayout()
  {
    wasLaidOut = true;
    snapToSize();
  }

  public void snapToSize()
  {
    setWidth(snapToWidth);
    setHeight(snapToHeight);
  }
}
