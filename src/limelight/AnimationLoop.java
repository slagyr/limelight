//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import limelight.util.NanoTimer;

public abstract class AnimationLoop
{
  private static final int MAX_ITERATIONS_WITHOUT_YIELDING = 5;
  private static final int MAX_EXTRA_UPDATES = 5;

  private int framesPerSecond;
  private int updatesPerSecond;
  private Thread loopThread;
  private volatile boolean running;
  private long sleepPeriod;
  private NanoTimer timer;
  protected int roundsWithoutYielding;
  private int missedUpdates;

  public AnimationLoop()
  {
    setFramesPerSecond(80);
    setUpdatesPerSecond(80);
    timer = new NanoTimer();
  }

  public int getFramesPerSecond()
  {
    return framesPerSecond;
  }

  public int getUpdatesPerSecond()
  {
    return updatesPerSecond;
  }

  public void setFramesPerSecond(int value)
  {
    framesPerSecond = value;
    sleepPeriod = 1000000000 / framesPerSecond;
  }

  public void setUpdatesPerSecond(int value)
  {
    updatesPerSecond = value;
  }

  public void start()
  {
    loopThread = new Thread(new SimpleRunnable());
    running = true;
    loopThread.start();
  }

  public boolean isRunning()
  {
    return running;
  }

  public long getSleepPeriod()
  {
    return sleepPeriod;
  }

  private class SimpleRunnable implements Runnable
  {
    public void run()
    {
      loop();
    }
  }

  public void stop()
  {
    running = false;
  }

  private void loop()
  {
    while(running)
    {
      timer.markTime();

      update();
      refresh();

      long adjustedSleepTime = sleepPeriod - timer.getIdleNanos() + timer.getSleepJiggle();
      if(adjustedSleepTime > 0)
      {
        timer.sleep(adjustedSleepTime);
        roundsWithoutYielding = 0;
      }
      else
      {
        catchUpOnUpdatesIfNeeded(adjustedSleepTime);
        yieldIfNeeded();
      }
    }
  }

  private void catchUpOnUpdatesIfNeeded(long adjustedSleepTime)
  {
    missedUpdates += (int)((adjustedSleepTime * -1) / sleepPeriod);
    for(int extraUpdates = 0; (missedUpdates > 0 && extraUpdates < MAX_EXTRA_UPDATES); missedUpdates--, extraUpdates++)
      update();
  }

  // MDM - We need to yield at some point to let other threads get some work done.
  private void yieldIfNeeded()
  {
    roundsWithoutYielding++;  
    if(roundsWithoutYielding > MAX_ITERATIONS_WITHOUT_YIELDING)
    {
      Thread.yield();
      roundsWithoutYielding = 0;
    }
  }

  protected abstract void update();
  protected abstract void refresh();
}
