package limelight.task;

import limelight.util.NanoTimer;

import java.util.LinkedList;

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

  public void add(Task task)
  {
    tasks.add(task);
    task.setEngine(this);
  }

  public LinkedList<Task> getTasks()
  {
    return tasks;
  }

  public void cycle()
  {
    int tasksToPerform = tasks.size();
    for(int currentTask = 0; currentTask < tasksToPerform; currentTask++)
    {
      Task task = tasks.removeFirst();
      if(task.isReady())
        task.perform();
      else
        tasks.add(task);
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
