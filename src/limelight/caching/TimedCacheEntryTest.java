//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.caching;

import junit.framework.TestCase;

public class TimedCacheEntryTest extends TestCase
{
  private TimedCacheEntry entry;

  public void setUp() throws Exception
  {
    entry = new TimedCacheEntry<String>("blah", 0.01);
  }

  public void testConstructor() throws Exception
  {
    assertEquals(0.01, entry.getTimeoutSeconds());
  }
  
  public void testIsExpired() throws Exception
  {
    assertEquals(false, entry.isExpired());

    Thread.sleep(100);

    assertEquals(true, entry.isExpired());
  }

  public void testRenew() throws Exception
  {
    Thread.sleep(100);
    
    entry.renew();

    assertEquals(false, entry.isExpired());
  }
  
  public void testIsExpiredIfValueIsNull() throws Exception
  {
    entry = new TimedCacheEntry<String>(null, 1);
    assertEquals(true, entry.isExpired());
  }
}
