package limelight.background;

import limelight.util.NanoTimer;

public abstract class IdleThreadLoop
{
  private boolean running;
  private boolean isIdle;
  private Thread thread;
  private final Object lock = new Object();

  private NanoTimer timer = new NanoTimer();
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
    });
    thread.start();
  }

  public void stop()
  {
    running = false;
    try
    {
      if(isIdle())
        thread.interrupt();
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

//timer.log("about to execute:                 " + this);
          execute();
          delay();
          executions++;
          checkExecutions();
        }
        catch(Exception e)
        {
          //TODO What to do with execption here?
          e.printStackTrace();
        }
      }
    }
  }

  private void idle()
  {
//timer.log("going idle:                       " + this);
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
//timer.log("back in gear:                     " + this);
  }

  public void go()
  {
    if(isIdle)
    {

//timer.log("getting back into gear:           " + this);
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
