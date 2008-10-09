package limelight;

public abstract class IdleThreadLoop
{
  private boolean running;
  private boolean isIdle;
  private Thread thread;
  private final Object lock = new Object();

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
          execute();
          delay();
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
}
