package limelight.task;

import limelight.util.NanoTimer;

import java.util.LinkedList;
import java.util.Iterator;

public class TaskEngine
{
  private final static int CyclesPerSecond = 100;

  private LinkedList<Task> tasks;
  private boolean running;
  private Thread thread;
  private boolean stopped;
  private NanoTimer timer;
  private long sleepPeriod;

  public TaskEngine()
  {
    tasks = new LinkedList<Task>();
    timer = new NanoTimer();
    sleepPeriod = 1000000000 / CyclesPerSecond;
  }

  public synchronized void add(Task task)
  {
    tasks.add(task);
    task.setEngine(this);
  }

  public synchronized void remove(Task task)
  {
    tasks.remove(task);
    task.setEngine(null);
  }

  public LinkedList<Task> getTasks()
  {
    return new LinkedList<Task>(tasks);
  }

  public void cycle()
  {
    for(Task task : getTasksToCycle())
    {
      if(task.isReady())
        performTask(task);
      else
        tasks.add(task);
    }
  }

  private synchronized LinkedList<Task> getTasksToCycle()
  {
    LinkedList<Task> list = new LinkedList<Task>(tasks);
    tasks.clear();
    return list;
  }

  private void performTask(Task task)
  {
    try
    {
      task.perform();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public void start()
  {
    thread = new Thread(new Runnable(){
      public void run()
      {
        loop();
      }
    });
    stopped = false;
    running = true;
    thread.start();
  }

  private void loop()
  {
    while(!stopped)
    {
      timer.markTime();

      cycle();

      long adjustedSleepTime = sleepPeriod - timer.getIdleNanos() + timer.getSleepJiggle();
      if(adjustedSleepTime > 0)
        timer.sleep(adjustedSleepTime);
      else
        Thread.yield();

    }
    running = false;
  }

  public void stop()
  {
    stopped = true;
  }

  public boolean isRunning()
  {
    return running;
  }

  public TaskEngine started()
  {
    start();
    return this;
  }
}
