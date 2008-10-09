package limelight;

import junit.framework.TestCase;

public class CacheCleanerLoopTest extends TestCase
{
  private CacheCleanerLoop loop;

  public void setUp() throws Exception
  {
    loop = new CacheCleanerLoop(); 
  }

  public void testShouldBeAnIdleLoopthread() throws Exception
  {
    assertEquals(true, loop instanceof IdleThreadLoop);
  }

  public void testShouldNeverGoIdle() throws Exception
  {
    assertEquals(false, loop.shouldBeIdle());
  }

  public void testThreadHasLowPriority() throws Exception
  {
    loop.start();
    assertEquals(Thread.MIN_PRIORITY, loop.getThread().getPriority());
    loop.stop();
  }
}

