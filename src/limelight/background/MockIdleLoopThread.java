package limelight.background;

public class MockIdleLoopThread extends IdleThreadLoop
{
  public boolean shouldBeIdle()
  {
    return true;
  }

  protected void execute()
  {
  }

  protected void delay()
  {
  }
}
