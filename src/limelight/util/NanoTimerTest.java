package limelight.util;

import junit.framework.TestCase;

public class NanoTimerTest extends TestCase
{
  private int ONE_MILLION = 1000000;
  private int TEN_MILLION = 10 * ONE_MILLION;
  private int NINE_MILLION = 9 * ONE_MILLION;
  private int ELEVEN_MILLION = 11 * ONE_MILLION;

  private NanoTimer timer;

  public void setUp() throws Exception
  {
    timer = new NanoTimer();
  }
  
  public void testStartTime() throws Exception
  {
    long now = System.nanoTime();
    long then = timer.getTimeOfLastActivity();
    assertEquals("" + (now - then), true, now - then < ONE_MILLION); // less than 1 milisecond
  }

  public void testIdleTime() throws Exception
  {
    assertEquals(true, timer.getIdleNanos() < ONE_MILLION);
    Thread.sleep(10);
    assertEquals(true, timer.getIdleNanos() > TEN_MILLION);
  }

  public void testSleeping()
  {
    long before = System.nanoTime();
    timer.sleep(TEN_MILLION);
    long after = System.nanoTime();
    long sleepDuration = after - before;

    assertEquals("actual sleep duration: " + sleepDuration, true, (sleepDuration > NINE_MILLION && sleepDuration < ELEVEN_MILLION));
    assertEquals(true, timer.getActualSleepDuration() > NINE_MILLION && timer.getActualSleepDuration() < ELEVEN_MILLION);
    assertEquals(true, timer.getIdleNanos() < ONE_MILLION);
    assertEquals(true, timer.getSleepJiggle() < ONE_MILLION);
  }

  public void testSleepDurationAndJiggle() throws Exception
  {
    for(int i = 0; i < 10; i++)
    {
      long before = System.nanoTime();
      timer.sleep(TEN_MILLION);
      long after = System.nanoTime();
      long sleepDuration = after - before;

//      System.err.println("sleepDuration = " + sleepDuration + " actual: " + timer.getActualSleepDuration() + " jiggle: " + timer.getSleepJiggle());

      if(sleepDuration > TEN_MILLION)
      {
        assertEquals(true, timer.getSleepJiggle() < 0);
      }
      else if(sleepDuration < TEN_MILLION)
      {
        assertEquals(true, timer.getSleepJiggle() > 0);
      }
      else
      {
        assertEquals(0, timer.getSleepJiggle());
      }
    }
  }

  public void testDoesntSleepIfDurationIsZeroOrLess() throws Exception
  {
    Thread.sleep(10);

    timer.sleep(-1234567890);
    long mark = timer.getIdleNanos();
    assertEquals(0, timer.getActualSleepDuration());
    assertEquals("time of last activity: " + mark, true, mark < ONE_MILLION);
    assertEquals(0, timer.getSleepJiggle());

    Thread.sleep(10);
    timer.sleep(TEN_MILLION);

    timer.sleep(0);
    mark = timer.getIdleNanos();
    assertEquals(0, timer.getActualSleepDuration());
    assertEquals("time of last activity: " + mark, true, mark < ONE_MILLION);
    assertEquals(0, timer.getSleepJiggle());
  }
}