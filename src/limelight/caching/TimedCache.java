//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.caching;

public class TimedCache<KEY, VALUE> extends Cache<KEY, VALUE>
{
  private final double timeoutSeconds;

  public TimedCache(double timeoutInSeconds)
  {
    timeoutSeconds = timeoutInSeconds;
  }

  protected CacheEntry<VALUE> createEntry(VALUE value)
  {
    return new TimedCacheEntry<VALUE>(value, timeoutSeconds);
  }

  public double getTimeoutSeconds()
  {
    return timeoutSeconds;
  }
}
