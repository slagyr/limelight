//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.api;

public class MockProduction implements Production
{
  private String name;
  public boolean allowShutdown;
  public boolean wasAskedIfAllowedToShutdown;
  public boolean wasClosed;
  public String lastMethodCalled;
  public Object[] lastMethodCallArgs;

  public MockProduction(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public boolean allowClose()
  {
    wasAskedIfAllowedToShutdown = true;
    return allowShutdown;
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
}
