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
