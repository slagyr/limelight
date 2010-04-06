//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.caching;

import junit.framework.TestCase;

public class CacheTest extends TestCase
{
  private Cache<String, String> cache;

  private class TestableCache extends Cache<String, String>
  {
    protected CacheEntry<String> createEntry(String value)
    {
      return new MockCacheEntry<String>(value);
    }
  }

  public void setUp() throws Exception
  {
    cache = new TestableCache();
  }

  public void testCachingAndRetrievingObjects() throws Exception
  {
    cache.cache("1", "One");

    assertEquals("One", cache.retrieve("1"));
  }

  public void testClean() throws Exception
  {
    cache.cache("1", "One");
    cache.cache("2", "Two");
    cache.cache("3", "expired entry");

    assertEquals(3, cache.getMap().size());

    cache.clean();

    assertEquals(2, cache.getMap().size());
  }

  public void testEntryRenewedWhenRetrieved() throws Exception
  {
    cache.cache("1", "One");
    assertEquals(false, MockCacheEntry.last.renewed);

    cache.retrieve("1");
    assertEquals(true, MockCacheEntry.last.renewed);
  }

  public void testRemovesNullEntriesOnRetrieval() throws Exception
  {
    cache.cache("1", null);  
    assertEquals(1, cache.getMap().size());

    cache.retrieve("1");
    assertEquals(0, cache.getMap().size());
  }
  
  public void testExplicitlyExpiringAnEntry() throws Exception
  {
    cache.cache("1", "One");

    cache.expire("1");

    assertEquals(null, cache.retrieve("1"));
  }
}
