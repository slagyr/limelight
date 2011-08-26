//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

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
