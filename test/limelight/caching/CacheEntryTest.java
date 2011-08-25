//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

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
