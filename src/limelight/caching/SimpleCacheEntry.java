//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

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
