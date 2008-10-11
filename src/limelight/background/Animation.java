package limelight.background;

import limelight.util.NanoTimer;
import limelight.Context;

public abstract class Animation
{
  private static int MaxMakeups = 5;
  
  private long delayNanos;
  private NanoTimer timer = new NanoTimer();
  private boolean running;
//  private int updates = 0;
//  private long lastCheckTime;

  public Animation(int updatesPerSecond)
  {
    setUpdatesPerSecond(updatesPerSecond);
    timer.setMark(delayNanos + 1);
  }

  protected abstract void doUpdate();

  public void setUpdatesPerSecond(int updatesPerSecond)
  {
    delayNanos = 1000000000 / updatesPerSecond;
  }

  public long getDelayNanos()
  {
    return delayNanos;
  }

  public void update()
  {
    makeupMissedUpdates();
    timer.markTime();
    doUpdate();
//    updates++;
//    checkUpdates();
  }

  public boolean isReady()
  {
    return nanosSinceLastUpdate() >= delayNanos;
  }

  public long nanosSinceLastUpdate()
  {
    return timer.getIdleNanos();
  }

  private void makeupMissedUpdates()
  {
    long missedUpdates = nanosSinceLastUpdate() / delayNanos;
    for(int i = 1; i < missedUpdates && i < (MaxMakeups + 1); i++)
    {
      doUpdate();
//      updates++;
    }
  }

  public void resetTimer()
  {
    timer.markTime();
  }

//  private void checkUpdates()
//  {
//    boolean timeSinceLastCheck = (System.nanoTime() - lastCheckTime) > (1000000000l);
//    if(timeSinceLastCheck)
//    {
//      if(lastCheckTime != 0)
////      System.err.println(getName() + " performances/sec: " + performances);
//      lastCheckTime = System.nanoTime();
//      updates = 0;
//    }
//  }

  public void start()
  {
    running = true;
    AnimationLoop loop = Context.instance().animationLoop;
    loop.add(this);
    loop.go();
  }

  public void stop()
  {
    Context.instance().animationLoop.remove(this);
    running = false;
  }

  public boolean isRunning()
  {
    return running;
  }
}
