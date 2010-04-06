//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.caching;

public class TimedCacheEntry<T> extends CacheEntry<T>
{
  private final double timeoutSeconds;
  private double expirationDate;

  public TimedCacheEntry(T value, double timeout)
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
    return value() == null || System.nanoTime() > expirationDate;
  }

  public double getTimeoutSeconds()
  {
    return timeoutSeconds;
  }
}
