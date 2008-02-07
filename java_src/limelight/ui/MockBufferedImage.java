package limelight.ui;

import java.awt.image.BufferedImage;

public class MockBufferedImage extends BufferedImage
{
  public int subImageY;
  public int subImageX;

  public MockBufferedImage()
  {
    super(100, 200, BufferedImage.TYPE_4BYTE_ABGR);
  }

  public MockBufferedImage(int width, int height)
  {
    super(width, height, BufferedImage.TYPE_4BYTE_ABGR);
  }

  public BufferedImage getSubimage(int x, int y, int w, int h)
  {
    subImageX = x;
    subImageY = y;
    return new MockBufferedImage(w, h);
  }
}
