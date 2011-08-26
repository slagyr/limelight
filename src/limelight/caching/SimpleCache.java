//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.caching;

public class SimpleCache<KEY, VALUE> extends Cache<KEY, VALUE>
{
  protected CacheEntry<VALUE> createEntry(VALUE value)
  {
    return new SimpleCacheEntry<VALUE>(value);
  }
}
