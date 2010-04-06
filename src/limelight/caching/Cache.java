//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.caching;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public abstract class Cache<KEY, VALUE>
{
  private final HashMap<KEY, CacheEntry<VALUE>> map;

  public Cache()
  {
    map = new HashMap<KEY, CacheEntry<VALUE>>();
  }

  synchronized public void cache(KEY key, VALUE value)
  {
    map.put(key, createEntry(value));
  }

  protected abstract CacheEntry<VALUE> createEntry(VALUE value);

  synchronized public VALUE retrieve(KEY key)
  {
    CacheEntry<VALUE> entry = map.get(key);
    if(entry != null)
    {
      VALUE value = entry.value();
      if(value != null)
        entry.renew();
      else
        map.remove(key);
      return value;
    }
    else
      return null;
  }

  public HashMap<KEY, CacheEntry<VALUE>> getMap()
  {
    return map;
  }

  synchronized public void clean()
  {
    ArrayList<KEY> deletes = new ArrayList<KEY>();
    for(Map.Entry<KEY, CacheEntry<VALUE>> mapEntry : map.entrySet())
    {
      if(mapEntry.getValue().isExpired())
        deletes.add(mapEntry.getKey());
    }

    for(KEY key : deletes)
      map.remove(key);
  }

  synchronized public void expire(KEY key)
  {
    map.remove(key);
  }
}
