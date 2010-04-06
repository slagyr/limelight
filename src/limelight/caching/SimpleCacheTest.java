//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.caching;

import junit.framework.TestCase;

public class SimpleCacheTest extends TestCase
{
  public void testCreateSimpleCacheEntries() throws Exception
  {
    SimpleCache<String, String> cache = new SimpleCache<String, String>();
    cache.cache("1", "one");

    assertEquals(SimpleCacheEntry.class, cache.getMap().get("1").getClass());
  }
}
