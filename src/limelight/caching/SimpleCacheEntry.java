//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.caching;

public class SimpleCacheEntry<T> extends CacheEntry<T>
{
  public SimpleCacheEntry(T value)
  {
    super(value);
  }

  public boolean isExpired()
  {
    return value() == null;
  }

  public void renew()
  {
  }
}
