package limelight.caching;

import junit.framework.TestCase;

public class TimedCacheTest extends TestCase
{
  private TimedCache cache;

  public void setUp() throws Exception
  {
    cache = new TimedCache(1);
  }
  
  public void testTimeout() throws Exception
  {
    assertEquals(1, cache.getTimeoutSeconds(), 0.01);
  }

  public void testCreatesTimesCacheEntries() throws Exception
  {
    cache.cache("1", "one");

    CacheEntry entry = cache.getMap().get("1");
    assertEquals(TimedCacheEntry.class, entry.getClass());
    assertEquals(1, ((TimedCacheEntry)entry).getTimeoutSeconds(), 0.01);
  }
}
