package limelight.caching;

import junit.framework.TestCase;

public class SimpleCacheTest extends TestCase
{
  public void testCreateSimpleCacheEntries() throws Exception
  {
    SimpleCache cache = new SimpleCache();
    cache.cache("1", "one");

    assertEquals(SimpleCacheEntry.class, cache.getMap().get("1").getClass());
  }
}
