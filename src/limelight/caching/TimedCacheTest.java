//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.caching;

import junit.framework.TestCase;

public class TimedCacheTest extends TestCase
{
  private TimedCache<String, String> cache;

  public void setUp() throws Exception
  {
    cache = new TimedCache<String, String>(1);
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
