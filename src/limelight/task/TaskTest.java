//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.task;

import junit.framework.TestCase;

public class TaskTest extends TestCase
{
  public void testEngine() throws Exception
  {
    Task task = new TestableTask("joe");

    assertNull(task.getEngine());
    TaskEngine engine = new TaskEngine();
    task.setEngine(engine);
    assertSame(engine, task.getEngine());
  }

  private static class TestableTask extends Task
  {
    public TestableTask(String name)
    {
      super(name);
    }

    public boolean isReady()
    {
      return true;
    }

    public void perform()
    {
    }
  }

  public void testName() throws Exception
  {
    Task task = new TestableTask("joe");
    assertEquals("joe", task.getName());
  }
}

