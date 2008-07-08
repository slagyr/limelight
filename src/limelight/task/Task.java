package limelight.task;

public abstract class Task
{
  private TaskEngine engine;

  public abstract boolean isReady();
  public abstract void perform();

  public TaskEngine getEngine()
  {
    return engine;
  }

  public void setEngine(TaskEngine engine)
  {
    this.engine = engine;
  }
}
