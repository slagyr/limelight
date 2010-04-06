//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.caching;

public class MockCacheEntry<T> extends CacheEntry<T>
{
  public boolean expired;
  public static MockCacheEntry last;
  public boolean renewed;

  public MockCacheEntry(T value)
  {
    super(value);
    last = this;
  }

  public boolean isExpired()
  {
    return value().toString().startsWith("expired");
  }

  public void renew()
  {
    renewed = true;
  }
}
