package limelight.caching;

import junit.framework.TestCase;

public class SimpleCacheEntryTest extends TestCase
{ 
  public void testNotExpiredIfValueNotNull() throws Exception
  {
    CacheEntry<String> entry = new SimpleCacheEntry<String>("blah");
    assertEquals(false, entry.isExpired());
  }

  public void testExpiredWhenValueIsNull() throws Exception
  {
    CacheEntry<String> entry = new SimpleCacheEntry<String>(null);
    assertEquals(true, entry.isExpired());
  }
}
