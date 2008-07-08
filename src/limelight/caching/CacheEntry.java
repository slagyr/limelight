package limelight.caching;

import java.lang.ref.SoftReference;

public abstract class CacheEntry
{
  private SoftReference<Object> entry;

  public CacheEntry(Object value)
  {
    entry = new SoftReference<Object>(value);
  }

  public Object value()
  {
    return entry.get();
  }

  public abstract boolean isExpired();
  public abstract void renew();
}
