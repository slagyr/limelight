package limelight.caching;

import junit.framework.TestCase;

public class SimpleCacheEntryTest extends TestCase
{ 
  public void testNotExpiredIfValueNotNull() throws Exception
  {
    CacheEntry entry = new SimpleCacheEntry("blah");
    assertEquals(false, entry.isExpired());
  }

  public void testExpiredWhenValueIsNull() throws Exception
  {
    CacheEntry entry = new SimpleCacheEntry(null);
    assertEquals(true, entry.isExpired());
  }
}
