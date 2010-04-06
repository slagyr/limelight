//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

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
