package limelight;

import limelight.util.NanoTimer;

public abstract class AnimationLoop
{
  private static final int MAX_ITERATIONS_WITHOUT_YIELDING = 5;

  private int framesPerSecond;
  private int updatesPerSecond;
  private Thread loopThread;
  private volatile boolean running;
  private long sleepPeriod;
  private NanoTimer timer;
  protected int roundsWithoutYielding;

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

      timer.sleep(sleepPeriod - timer.getIdleNanos() + timer.getSleepJiggle());
      yieldIfNeeded(timer.getActualSleepDuration());
    }
  }

  // MDM - We need to yield at some point to let other threads get some work done.
  private void yieldIfNeeded(long actualSleepDuration)
  {
    if(actualSleepDuration == 0)
    {
      roundsWithoutYielding++;
      if(roundsWithoutYielding > MAX_ITERATIONS_WITHOUT_YIELDING)
      {
        Thread.yield();
        roundsWithoutYielding = 0;
      }
    }
    else
      roundsWithoutYielding = 0;
  }

  protected abstract void update();
  protected abstract void refresh();
}
