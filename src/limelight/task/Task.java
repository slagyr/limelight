package limelight.task;

public abstract class Task
{
  private TaskEngine engine;

  public void prepare()
  {
    // do nothing by default
  }

  public abstract void perform();

  public void conclude()
  {
    // do nothing by default
  }

  public TaskEngine getEngine()
  {
    return engine;
  }

  public void setEngine(TaskEngine engine)
  {
    this.engine = engine;
  }
}
