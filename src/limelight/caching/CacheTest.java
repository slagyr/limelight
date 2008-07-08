package limelight.caching;

import junit.framework.TestCase;

public class CacheTest extends TestCase
{
  private Cache cache;

  private class TestableCache extends Cache
  {
    protected CacheEntry createEntry(Object value)
    {
      return new MockCacheEntry(value);
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
}
