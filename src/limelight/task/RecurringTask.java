//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.task;

import limelight.util.NanoTimer;

public abstract class RecurringTask extends Task
{
  private static int MaxMakeups = 5;

  private long delay;
  private NanoTimer timer;
  private boolean strict;
  private double performancesPerSecond;
  private int performances;
  private long lastPerformanceCheckTime;

  public RecurringTask(String name, int performancesPerSecond)
  {
    super(name);
    setPerformancesPerSecond(performancesPerSecond);
    timer = new NanoTimer();
  }

  public long getDelayNano()
  {
    return delay;
  }

  public void setPerformancesPerSecond(int performancesPerSecond)
  {
    this.performancesPerSecond = performancesPerSecond;
    delay = 1000000000l / performancesPerSecond;
  }

  public boolean isReady()
  {
    return nanosSinceLastPerformance() >= delay;
  }

  public void perform()
  {
    if(strict)
      makeupMissedPerformances();
    timer.markTime();
    doPerform();
    performances++;
    checkPerformances();
    if(getEngine() != null)
      getEngine().add(this);
  }

  private void checkPerformances()
  {
    boolean timeSinceLastCheck = (System.nanoTime() - lastPerformanceCheckTime) > (1000000000l);
    if(timeSinceLastCheck)
    {
      if(lastPerformanceCheckTime != 0)
//      System.err.println(getName() + " performances/sec: " + performances);
      lastPerformanceCheckTime = System.nanoTime();
      performances = 0;

    }
  }

  private void makeupMissedPerformances()
  {
    long missedPerformances = nanosSinceLastPerformance() / delay;
    for(int i = 1; i < missedPerformances && i < (MaxMakeups + 1); i++)
    {
      doPerform();
      performances++;
    }
  }

  protected abstract void doPerform();

  public long nanosSinceLastPerformance()
  {
    return timer.getIdleNanos();
  }

  public void setStrict(boolean strict)
  {
    this.strict = strict;
  }

  public boolean isStrict()
  {
    return strict;
  }

  public double getPerformancesPerSecond()
  {
    return performancesPerSecond;
  }
}
