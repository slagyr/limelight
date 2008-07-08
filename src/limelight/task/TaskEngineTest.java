package limelight.task;

import junit.framework.TestCase;

public class TaskEngineTest extends TestCase
{
  private TaskEngine engine;
  private MockTask task;

  public void setUp() throws Exception
  {
    engine = new TaskEngine();
    task = new MockTask();
  }

  public void testAddingTasks() throws Exception
  {
    engine.add(task);

    assertEquals(1, engine.getTasks().size());
    assertSame(task, engine.getTasks().get(0));
  }

  public void testSetsTasksEngineWhenAdding() throws Exception
  {
    engine.add(task);

    assertSame(engine, task.getEngine());
  }

  public void testRunOneTask() throws Exception
  {
    engine.add(task);

    engine.cycle();

    assertEquals(0, engine.getTasks().size());
    assertEquals(true, task.prepared);
    assertEquals(true, task.performed);
    assertEquals(true, task.concluded);
  }

  public void testRunMultipleTasks() throws Exception
  {
    MockTask task2 = new MockTask();
    MockTask task3 = new MockTask();
    engine.add(task);
    engine.add(task2);
    engine.add(task3);

    engine.cycle();

    assertEquals(0, engine.getTasks().size());
    assertEquals(true, task.prepared);
    assertEquals(true, task.performed);
    assertEquals(true, task.concluded);
    assertEquals(true, task2.prepared);
    assertEquals(true, task2.performed);
    assertEquals(true, task2.concluded);
    assertEquals(true, task3.prepared);
    assertEquals(true, task3.performed);
    assertEquals(true, task3.concluded);
  }

  public void testTasksAddedDuringCycleAreNotPerformed() throws Exception
  {
    final MockTask task2 = new MockTask();
    task.onPerform = new Runnable()
    {
      public void run()
      {
        engine.add(task2);
      }
    };
    engine.add(task);

    engine.cycle();

    assertEquals(1, engine.getTasks().size());
    assertEquals(task2, engine.getTasks().get(0));
    assertEquals(true, task.prepared);
    assertEquals(true, task.performed);
    assertEquals(true, task.concluded);
    assertEquals(false, task2.prepared);
    assertEquals(false, task2.performed);
    assertEquals(false, task2.concluded);
  }

  public void testStartingAndStopping() throws Exception
  {
    engine.start();
    assertEquals(true, engine.isRunning());

    Thread.sleep(50);
    engine.stop();
    Thread.sleep(50);
    assertEquals(false, engine.isRunning());
  }

  public void testRunsAbout100TimesPerSecond() throws Exception
  {
    task.onPerform = new Runnable() {
      public void run()
      {
        engine.add(task);
      }
    };
    engine.add(task);

    engine.start();
    Thread.sleep(1000);
    engine.stop();
    Thread.sleep(10);

    assertEquals("actual performances: " + task.performances, true, task.performances > 95 && task.performances < 105);
  }
}
