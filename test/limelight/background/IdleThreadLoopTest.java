//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.background;

import junit.framework.TestCase;

public class IdleThreadLoopTest extends TestCase
{
  private TestableIdleThreadLoop loop;

  private static class TestableIdleThreadLoop extends IdleThreadLoop
  {
    public boolean shouldIdle;
    public int executions;
    public final long delay = 0;

    public boolean shouldBeIdle()
    {
      return shouldIdle;
    }

    protected void execute()
    {
      executions += 1;
    }

    protected void delay()
    {
      try
      {
        Thread.sleep(delay);
      }
      catch(InterruptedException e)
      {
        //okay
      }
    }
  }

  public void setUp() throws Exception
  {
    loop = new TestableIdleThreadLoop();
    loop.shouldIdle = true;
  }

  public void tearDown() throws Exception
  {
    if(loop.isRunning())
    {
      Thread stopThread = new Thread(new Runnable() {
        public void run()
        {
          loop.stop();
        }
      });
      stopThread.start();
      stopThread.join(1000);
      if(loop.getThread().isAlive())
      {
//        System.err.println("IdleThreadLoopTest: interrupting loop because it wont stop.");
        loop.getThread().interrupt();
      }
    }
  }

  public void testStartsInIdleWhenShouldIdleReturnsTrue() throws Exception
  {
    assertEquals(false, loop.isRunning());

    loop.start();

    Thread.sleep(10);
    assertEquals(true, loop.isRunning());
    assertEquals(true, loop.isIdle());
  }

  public void testStartsInGearWhenShouldIdleReturnsFalse() throws Exception
  {
    loop.shouldIdle = false;
    
    loop.start();

    assertEquals(true, loop.isRunning());
    assertEquals(false, loop.isIdle());
  }

  public void testDoesNotExecuteWhileIdle() throws Exception
  {
    loop.shouldIdle = true;
    loop.start();

    Thread.sleep(10);
    assertEquals(0, loop.executions);
  }

  public void testDoesExecuteWhileInGear() throws Exception
  {
    loop.shouldIdle = false;
    loop.start();

    Thread.sleep(10);
    assertEquals(true, loop.executions > 0);  
  }

  public void testCanGoFromGearToIdle() throws Exception
  {
    loop.shouldIdle = false;
    loop.start();

    Thread.sleep(10);
    loop.shouldIdle = true;
    Thread.sleep(10);

    int count = loop.executions;
    assertEquals(true, loop.isIdle());
    Thread.sleep(10);
    assertEquals(count, loop.executions);
  }

  public void testCanGoFromIdleToInGear() throws Exception
  {
    loop.shouldIdle = true;
    loop.start();
    Thread.sleep(10);

    loop.shouldIdle = false;
    loop.go();
    Thread.sleep(10);

    assertEquals(false, loop.isIdle());
    assertEquals(true, loop.executions > 0);
  }

  public void testStopAndGoManyTimes() throws Exception
  {
    loop.shouldIdle = true;
    loop.start();
    int executionCount = 0;
    for(int i = 0; i < 10; i++)
    {
      loop.shouldIdle = false;
      loop.go();
      Thread.sleep(10);
      assertEquals(false, loop.isIdle());
      loop.shouldIdle = true;
      Thread.sleep(10);
      assertEquals(true, loop.isIdle());
      assertEquals(true, loop.executions > executionCount);
      executionCount = loop.executions;
    }
  }

  public void testStopping() throws Exception
  {
    loop.shouldIdle = false;
    loop.start();

    loop.stop();

    assertEquals(false, loop.isRunning());
  }

  public void testDoesNotExecutingFromGoingFromIdleToStop() throws Exception
  {
    loop.shouldIdle = false;
    loop.start();

    Thread.sleep(10);
    loop.shouldIdle = true;
    Thread.sleep(10);
    int executionCount = loop.executions;

    loop.stop();
    assertEquals(false, loop.isRunning());
    assertEquals(executionCount, loop.executions);
  }
}
