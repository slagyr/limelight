//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.background;

import limelight.LimelightException;
import limelight.util.NanoTimer;
import limelight.Context;

// TODO MDM - Animations should stop if they throw an exception

public abstract class Animation
{
  private static final int MaxMakeups = 5;
  private static final double ONE_BILLION = 1000000000.0;

  private long delayNanos;
  private final NanoTimer timer = new NanoTimer();
  private boolean running;
  private long tolerableDelay;

  public Animation(double updatesPerSecond)
  {
    setUpdatesPerSecond(updatesPerSecond);
    timer.setMark(delayNanos + 1);
  }

  protected abstract void doUpdate();

  public void setUpdatesPerSecond(double updatesPerSecond)
  {
    if (updatesPerSecond < 0.01)
    {
      delayNanos = Long.MAX_VALUE;
      stop();
    }
    else
    {
      delayNanos = (long) (ONE_BILLION / updatesPerSecond);
      tolerableDelay = (long) (delayNanos * 0.95);
    }
  }

  public double getUpdatesPerSecond()
  {
    if(delayNanos == Long.MAX_VALUE)
      return 0.0;
    return ONE_BILLION / delayNanos;
  }

  public long getDelayNanos()
  {
    return delayNanos;
  }

  public void update()
  {
    makeupMissedUpdates();
    timer.markTime();
    try
    {
      doUpdate();
    }
    catch(Exception e)
    {
      stop();
      throw new LimelightException("Animation Cancelled", e);
    }
  }

  public boolean isReady()
  {
    long nanos = nanosSinceLastUpdate();
    return nanos >= tolerableDelay;
  }

  public long nanosSinceLastUpdate()
  {
    return timer.getIdleNanos();
  }

  private void makeupMissedUpdates()
  {
    long missedUpdates = nanosSinceLastUpdate() / delayNanos;
    for (int i = 1; i < missedUpdates && i < (MaxMakeups + 1); i++)
    {
      doUpdate();
    }
  }

  public void resetTimer()
  {
    timer.markTime();
  }

  public NanoTimer getTimer()
  {
    return timer;
  }

  public void start()
  {
    if(delayNanos == Long.MAX_VALUE)
      return;
    
    running = true;
    AnimationLoop loop = Context.instance().animationLoop;
    if (loop == null)
      return;
    loop.add(this);
    loop.go();
  }

  public void stop()
  {
    final AnimationLoop loop = Context.instance().animationLoop;
    if (loop != null)
      loop.remove(this);
    running = false;
  }

  public boolean isRunning()
  {
    return running;
  }

  public long getTolerableDelay()
  {
    return tolerableDelay;
  }
}
