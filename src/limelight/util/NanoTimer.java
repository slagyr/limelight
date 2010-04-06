//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

public class NanoTimer
{
  private long timeOfLastActivity;
  private long actualSleepDuration;
  private long sleepJiggle;

  public NanoTimer()
  {
    markTime();
  }

  public void markTime()
  {
    timeOfLastActivity = System.nanoTime();
  }

  public void setMark(long nanosAgo)
  {
    this.timeOfLastActivity = System.nanoTime() - nanosAgo;
  }

  public long getTimeOfLastActivity()
  {
    return timeOfLastActivity;
  }

  public long getIdleNanos()
  {
    return System.nanoTime() - timeOfLastActivity;
  }

  public void sleep(long nanosToSleep)
  {
    if(nanosToSleep > 0)
      performSleep(nanosToSleep);
    else
    {
      Thread.yield();
      markTime();
      actualSleepDuration = 0;
      sleepJiggle = 0;
    }
  }

  private void performSleep(long nanosToSleep)
  {
    long milisToSleep = nanosToSleep / 1000000l;
    int remainder = (int) (nanosToSleep % 1000000);
    markTime();
    try
    {
      Thread.sleep(milisToSleep, remainder);
    }
    catch(InterruptedException e)
    {
      e.printStackTrace();
    }
    long beforeSleep = timeOfLastActivity;
    markTime();
    actualSleepDuration = timeOfLastActivity - beforeSleep;
    sleepJiggle = nanosToSleep - actualSleepDuration;
  }

  public long getActualSleepDuration()
  {
    return actualSleepDuration;
  }

  public long getSleepJiggle()
  {
    return sleepJiggle;
  }

  public void moveMarkBackInTime(long nanos)
  {
    timeOfLastActivity -= nanos;
  }
}
