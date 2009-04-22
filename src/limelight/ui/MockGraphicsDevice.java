package limelight.ui;

import java.awt.*;

public class MockGraphicsDevice extends GraphicsDevice
{
  private MockGraphicsConfiguration defaultConfiguration;

  public MockGraphicsDevice()
  {
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
}
