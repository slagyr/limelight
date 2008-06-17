package limelight;

import junit.framework.TestCase;

import java.util.LinkedList;

public class AnimationLoopTest extends TestCase
{
  private TestableAnimationLoop loop;

  private class TestableAnimationLoop extends AnimationLoop
  {
    public int updates;
    public int refreshes;
    public Runnable refreshRunnable;

    protected void update()
    {
      updates++;
    }

    protected void refresh()
    {
      if(refreshRunnable != null)
        refreshRunnable.run();
      refreshes++;
    }

    public int getRoundsWithoutYielding()
    {
      return roundsWithoutYielding;
    }

  }

  private class HelpfulRunnable implements Runnable
  {
    private int delay;
    public LinkedList<Integer> record;

    public HelpfulRunnable(int delay)
    {
      this.delay = delay;
      record = new LinkedList<Integer>();
    }

    public void run()
    {
      try
      {
        Thread.sleep(delay);      
        record.add(loop.getRoundsWithoutYielding());
      }
      catch(InterruptedException e)
      {
        e.printStackTrace();
      }
    }
  }

  public void setUp() throws Exception
  {
    loop = new TestableAnimationLoop();
  }
  
  public void testSettingsDefaults() throws Exception
  {
    assertEquals(80, loop.getFramesPerSecond());
    assertEquals(80, loop.getUpdatesPerSecond());
  }

  public void testSettingSettings() throws Exception
  {
    loop.setFramesPerSecond(100);
    assertEquals(100, loop.getFramesPerSecond());

    loop.setUpdatesPerSecond(100);
    assertEquals(100, loop.getUpdatesPerSecond());
  }
  
  public void testRunning() throws Exception
  {
    assertEquals(false, loop.isRunning());
    loop.start();
    assertEquals(true, loop.isRunning());
    loop.stop();
    assertEquals(false, loop.isRunning());
  }

  public void testRunningForShortTimeCallUpdateAndRefresh() throws Exception
  {
    loop.start();
    Thread.sleep(10);
    loop.stop();

    assertEquals(true, loop.updates > 0);
    assertEquals(true, loop.refreshes > 0);
  }

  public void testNanosToSleep() throws Exception
  {
    loop.setFramesPerSecond(10);
    assertEquals(100 * 1000000, loop.getSleepPeriod());

    loop.setFramesPerSecond(100);
    assertEquals(10 * 1000000, loop.getSleepPeriod());
  }

  public void testFramesPerSecondIsCloselyUpheld() throws Exception
  {
    loop.start();
    Thread.sleep(100);
    loop.stop();

    assertEquals("Expected 8 refreshes but got " + loop.refreshes, true, loop.refreshes > 6 && loop.refreshes < 10);
    assertEquals("Expected 8 updates but got " + loop.updates, true, loop.updates > 6 && loop.updates < 10); // should be 8
  }

  public void testFramesPerSecondWhenPaitingIsTimerConsuming() throws Exception
  {
    loop.refreshRunnable = new HelpfulRunnable(10);
    loop.start();
    Thread.sleep(100);
    loop.stop();

    assertEquals("Expected 8 refreshes but got " + loop.refreshes, true, loop.refreshes > 6 && loop.refreshes < 10);
    assertEquals("Expected 8 updates but got " + loop.updates, true, loop.updates > 6 && loop.updates < 10); // should be 8
  }

  public void testRoundsWithoutYielding() throws Exception
  {
    HelpfulRunnable runnable = new HelpfulRunnable(20);
    loop.refreshRunnable = runnable;
    loop.start();
    Thread.sleep(200);
    loop.stop();

    assertEquals("record has " + runnable.record.size() + " entries", true, runnable.record.size() > 5);
    assertEquals(0, (int)runnable.record.get(0));
    assertEquals(1, (int)runnable.record.get(1));
    assertEquals(2, (int)runnable.record.get(2));
    assertEquals(3, (int)runnable.record.get(3));
    assertEquals(4, (int)runnable.record.get(4));
    assertEquals(5, (int)runnable.record.get(5));
    assertEquals(0, (int)runnable.record.get(6));
  }
}
