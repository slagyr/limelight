package limelight.caching;

public class SimpleCache extends Cache
{
  protected CacheEntry createEntry(Object value)
  {
    return new SimpleCacheEntry(value);
  }
}
