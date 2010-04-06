//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.background;

import limelight.Context;

public class CacheCleanerLoop extends IdleThreadLoop
{
  public boolean shouldBeIdle()
  {
    return false;
  }

  protected void execute()
  {   
    if(Context.instance().bufferedImageCache != null)
      Context.instance().bufferedImageCache.clean();
    if(Context.instance().bufferedImagePool != null)
      Context.instance().bufferedImagePool.clean();
  }

  public void start()
  {
    super.start();
    getThread().setPriority(Thread.MIN_PRIORITY);
  }

  protected void delay()
  {
    try
    {
      Thread.sleep(1000);
    }
    catch(InterruptedException e)
    {
      // okay
    }
  }

  public CacheCleanerLoop started()
  {
    start();
    return this;
  }
}
