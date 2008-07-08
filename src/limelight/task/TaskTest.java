package limelight.task;

import junit.framework.TestCase;

public class TaskTest extends TestCase
{
  public void testEngine() throws Exception
  {
    Task task = new Task(){
      public boolean isReady()
      {
        return true;
      }

      public void perform()
      {
      }
    };

    assertNull(task.getEngine());
    TaskEngine engine = new TaskEngine();
    task.setEngine(engine);
    assertSame(engine, task.getEngine());
  }
}
