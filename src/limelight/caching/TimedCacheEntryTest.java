package limelight.caching;

import junit.framework.TestCase;

public class TimedCacheEntryTest extends TestCase
{
  private TimedCacheEntry entry;

  public void setUp() throws Exception
  {
    entry = new TimedCacheEntry("blah", 0.01);
  }

  public void testConstructor() throws Exception
  {
    assertEquals(0.01, entry.getTimeoutSeconds());
  }
  
  public void testIsExpired() throws Exception
  {
    assertEquals(false, entry.isExpired());

    Thread.sleep(100);

    assertEquals(true, entry.isExpired());
  }

  public void testRenew() throws Exception
  {
    Thread.sleep(100);
    
    entry.renew();

    assertEquals(false, entry.isExpired());
  }
}
