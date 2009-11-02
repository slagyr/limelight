//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

import java.awt.*;

public class MockGraphicsDevice extends GraphicsDevice
{
  public MockGraphicsConfiguration defaultConfiguration;
  public Window internalFullScreenWindow;

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

  public void setFullScreenWindow(Window window)
  {
    internalFullScreenWindow = window;
  }

  public Window getFullScreenWindow()
  {
    return internalFullScreenWindow;
  }
}
