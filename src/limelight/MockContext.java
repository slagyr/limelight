package limelight;

public class MockContext extends Context
{
  public boolean shutdownAttempted;

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
}
