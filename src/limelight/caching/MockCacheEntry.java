package limelight.caching;

public class MockCacheEntry extends CacheEntry
{
  public boolean expired;
  public static MockCacheEntry last;
  public boolean renewed;

  public MockCacheEntry(Object value)
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
