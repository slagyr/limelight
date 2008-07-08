package limelight.caching;

public class TimedCacheEntry extends CacheEntry
{
  private double timeoutSeconds;
  private double expirationDate;

  public TimedCacheEntry(Object value, double timeout)
  {
    super(value);
    timeoutSeconds = timeout;
    renew();
  }

  public void renew()
  {
    expirationDate = System.nanoTime() + 1000000000 * timeoutSeconds;
  }

  public boolean isExpired()
  {
    return System.nanoTime() > expirationDate;
  }

  public double getTimeoutSeconds()
  {
    return timeoutSeconds;
  }
}
