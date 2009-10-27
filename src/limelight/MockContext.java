//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

public class MockContext extends Context
{
  public boolean shutdownAttempted;
  public boolean wasShutdown;

  private MockContext()
  {
    super();
  }

  public static MockContext stub()
  {
    MockContext mockContext = new MockContext();
    Context.instance = mockContext;
    return mockContext;
  }

  public void attemptShutdown()
  {
    shutdownAttempted = true;
  }

  public void shutdown()
  {
    wasShutdown = true;
  }
}
