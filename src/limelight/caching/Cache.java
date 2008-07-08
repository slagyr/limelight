package limelight.caching;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public abstract class Cache
{
  private HashMap<Object, CacheEntry> map;

  public Cache()
  {
    map = new HashMap<Object, CacheEntry>();
  }

  public void cache(Object key, Object value)
  {
    map.put(key, createEntry(value));
  }

  protected abstract CacheEntry createEntry(Object value);

  public Object retrieve(Object key)
  {
    CacheEntry entry = map.get(key);
    if(entry != null)
    {
      Object value = entry.value();
      if(value != null)
        entry.renew();
      else
        map.remove(key);
      return value;
    }
    else
      return null;
  }

  public HashMap<Object, CacheEntry> getMap()
  {
    return map;
  }

  public void clean()
  {
    ArrayList deletes = new ArrayList();
    for(Map.Entry<Object, CacheEntry> mapEntry : map.entrySet())
    {
      if(mapEntry.getValue().isExpired())
        deletes.add(mapEntry.getKey());
    }

    for(Object key : deletes)
      map.remove(key);
  }
}
