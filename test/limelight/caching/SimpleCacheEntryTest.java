//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

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
