package limelight.ui;

import apple.awt.CGraphicsDevice;

import java.awt.*;

public class MockGraphicsDevice extends CGraphicsDevice
{
  public MockGraphicsConfiguration defaultConfiguration;
  public Window internalFullScreenWindow;

  public MockGraphicsDevice()
  {
    super(0);
    defaultConfiguration = new MockGraphicsConfiguration(this); 
  }

  public int getType()
  {
    return 0;
  }

  public String getIDstring()
  {
    return "Mock";
  }

  public GraphicsConfiguration[] getConfigurations()
  {
    return new GraphicsConfiguration[]{defaultConfiguration};
  }

  public GraphicsConfiguration getDefaultConfiguration()
  {
    return defaultConfiguration;
  }

  public void setFullScreenWindow(Window window)
  {
    internalFullScreenWindow = window;
  }

  public Window getFullScreenWindow()
  {
    return internalFullScreenWindow;
  }
}
