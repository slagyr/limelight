package limelight.caching;

public class SimpleCache<KEY, VALUE> extends Cache<KEY, VALUE>
{
  protected CacheEntry<VALUE> createEntry(VALUE value)
  {
    return new SimpleCacheEntry<VALUE>(value);
  }
}
