package limelight.caching;

public class TimedCache extends Cache
{
  private double timeoutSeconds;

  public TimedCache(double timeoutInSeconds)
  {
    timeoutSeconds = timeoutInSeconds;
  }

  protected CacheEntry createEntry(Object value)
  {
    return new TimedCacheEntry(value, timeoutSeconds);
  }

  public double getTimeoutSeconds()
  {
    return timeoutSeconds;
  }
}
