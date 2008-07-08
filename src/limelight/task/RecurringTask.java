package limelight.task;

import limelight.util.NanoTimer;

public abstract class RecurringTask extends Task
{
  private static int MaxMakeups = 5;

  private long delay;
  private NanoTimer timer;
  private boolean strict;

  public RecurringTask(int performancesPerSecond)
  {
    setPerformancesPerSecond(performancesPerSecond);
    timer = new NanoTimer();
  }

  public long getDelayNano()
  {
    return delay;
  }

  public void setPerformancesPerSecond(int performancesPerSecond)
  {
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
  }

  private void makeupMissedPerformances()
  {
    long missedPerformances = nanosSinceLastPerformance() / delay;
    for(int i = 1; i < missedPerformances && i < (MaxMakeups + 1); i++)
      doPerform();  
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
}
