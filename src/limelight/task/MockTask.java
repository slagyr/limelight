package limelight.task;

public class MockTask extends Task
{
  public boolean concluded;
  public boolean prepared;
  public boolean performed;
  public Runnable onPerform;
  public int performances;

  public void prepare()
  {
    prepared = true;
  }

  public void perform()
  {
    performed = true;
    if(onPerform != null)
      onPerform.run();
    performances++;
  }

  public void conclude()
  {
    concluded = true;
  }

}
