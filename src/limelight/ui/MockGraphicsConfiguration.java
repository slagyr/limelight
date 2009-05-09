package limelight.ui;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.awt.image.ColorModel;

public class MockGraphicsConfiguration extends GraphicsConfiguration
{
  private MockGraphicsDevice graphicsDevice;
  private MockVolatileImage volatileImage;

  public MockGraphicsConfiguration(MockGraphicsDevice graphicsDevice)
  {
    this.graphicsDevice = graphicsDevice;
    volatileImage = new MockVolatileImage();
  }

  public GraphicsDevice getDevice()
  {
    return graphicsDevice;
  }

  public BufferedImage createCompatibleImage(int width, int height)
  {
    return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
  }

  public VolatileImage createCompatibleVolatileImage(int width, int height)
  {
    return volatileImage;
  }

  public VolatileImage createCompatibleVolatileImage(int width, int height, int transparency)
  {
    return volatileImage;
  }

  public BufferedImage createCompatibleImage(int width, int height, int transparency)
  {
    return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
  }

  public ColorModel getColorModel()
  {
    return ColorModel.getRGBdefault();
  }

  public ColorModel getColorModel(int transparency)
  {
    return ColorModel.getRGBdefault();
  }

  public AffineTransform getDefaultTransform()
  {
    return new AffineTransform();
  }

  public AffineTransform getNormalizingTransform()
  {
    return new AffineTransform();
  }

  public Rectangle getBounds()
  {
    return new Rectangle(1000, 1000);
  }
}
