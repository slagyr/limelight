package limelight.caching;

public class SimpleCacheEntry extends CacheEntry
{
  public SimpleCacheEntry(Object value)
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
