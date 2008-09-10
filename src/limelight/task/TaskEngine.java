//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.task;

import limelight.util.NanoTimer;

import java.util.ArrayList;
import java.util.List;

public class TaskEngine
{
  private final static int CyclesPerSecond = 100;

  private ArrayList<Task> tasks;
  private boolean running;
  private Thread thread;
  private boolean stopped;
  private NanoTimer timer;
  private long sleepPeriod;
  private ArrayList<Task> taskBuffer;

  public TaskEngine()
  {
    tasks = new ArrayList<Task>();
    taskBuffer = new ArrayList<Task>(50);
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

  public List<Task> getTasks()
  {
    return new ArrayList<Task>(tasks);
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

  private synchronized List<Task> getTasksToCycle()
  {
    taskBuffer.clear();
    taskBuffer.addAll(tasks);
    tasks.clear();
    return taskBuffer;
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
    try
    {
      thread.join();
    }
    catch(InterruptedException e)
    {
      //do nothing
    }
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
