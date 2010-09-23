//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.model;

import limelight.ui.api.MockCastingDirector;

public class MockProduction extends Production
{
  public boolean wasAskedIfAllowedToShutdown;
  public boolean wasClosed;
  public String lastMethodCalled;
  public Object[] lastMethodCallArgs;
  public int drbPort;

  public MockProduction(String name)
  {
    super(name);
    setCastingDirector(new MockCastingDirector());
  }

  public MockProduction()
  {
    this("test");
  }

  public boolean allowClose()
  {
    wasAskedIfAllowedToShutdown = true;
    return super.allowClose();
  }

  public void close()
  {
    wasClosed = true;
  }

  public Object callMethod(String name, Object... args)
  {
    lastMethodCalled = name;
    lastMethodCallArgs = args;
    return null;
  }

  public void publish_on_drb(int port)
  {
    drbPort = port;
  }
}
