package limelight.caching;

import junit.framework.TestCase;

public class CacheEntryTest extends TestCase
{
  private class TestableCahceEntry extends CacheEntry<Object>
  {
    public TestableCahceEntry(Object value)
    {
      super(value);
    }

    public boolean isExpired()
    {
      return false;
    }

    public void renew()
    {
    }
  }

  public void testCreation() throws Exception
  {
    CacheEntry entry = new TestableCahceEntry("blah");
    assertEquals("blah", entry.value());
  }
}
