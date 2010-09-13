//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.background;

public abstract class IdleThreadLoop
{
  public static boolean verbose = true;

  private boolean running;
  private boolean isIdle;
  private Thread thread;

  private final Object lock = new Object();
  private long lastCheckTime;
  private int executions;

  public abstract boolean shouldBeIdle();

  protected abstract void execute();

  protected abstract void delay();

  public IdleThreadLoop()
  {
    isIdle = false;
  }

  public Thread getThread()
  {
    return thread;
  }

  public boolean isIdle()
  {
    return isIdle;
  }

  public void start()
  {
    running = true;
    thread = new Thread(new Runnable()
    {
      public void run()
      {
        loop();
      }
    }, getClass().getName());
    thread.start();
  }

  public void stop()
  {
    running = false;
    try
    {
      if(isIdle())
        thread.interrupt();
      if(thread != null)
        thread.join();
    }
    catch(InterruptedException e)
    {
      //okay
    }
  }

  public boolean isRunning()
  {
    return running;
  }

  public void loop()
  {
    while(running)
    {
      if(shouldBeIdle())
        idle();
      if(running)
      {
        try
        {
          execute();
          delay();
          executions++;
          checkExecutions();
        }
        catch(Exception e)
        {
          //TODO What to do with execption here?
          if(verbose)
            e.printStackTrace();
        }
      }
    }
  }

  protected void idle()
  {
    isIdle = true;   
    synchronized(lock)
    {
      try
      {
        lock.wait();
      }
      catch(InterruptedException e)
      {
        //okay
      }
    }
  }

  public void go()
  {
    if(isIdle)
    {
      isIdle = false;
      synchronized(lock)
      {
        lock.notify();
      }
    }
  }

  private void checkExecutions()
  {
    boolean timeSinceLastCheck = (System.nanoTime() - lastCheckTime) > (1000000000l);
    if(timeSinceLastCheck)
    {
      if(lastCheckTime != 0)
//System.err.println(getClass().getName() + " executions/sec: " + executions);
      lastCheckTime = System.nanoTime();
      executions = 0;
    }
  }
}
