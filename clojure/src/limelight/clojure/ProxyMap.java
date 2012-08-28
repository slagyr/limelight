//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.clojure;

import clojure.lang.*;
import limelight.util.Opts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
 * I know... It's not really a "Persistent" map.  But this class is only used in cases where the
 * map will *never* change.  So the performance benefits of non-persistent map are appealing.
 * TODO - Make java side option maps persistent?
 */
public class ProxyMap extends APersistentMap
{
  private HashMap<String, Object> map;

  public ProxyMap()
  {
    map = new Opts();
  }

  public ProxyMap(HashMap<String, Object> map)
  {
    this.map = map;
  }

  public Map<String, Object> getMap()
  {
    return map;
  }

  public boolean containsKey(Object key)
  {
    return map.containsKey(stringify(key));
  }

  public IMapEntry entryAt(Object keyObj)
  {
    String key = stringify(keyObj);
    if(map.containsKey(key))
      return new MapEntry(key, map.get(key));
    else
      return null;
  }

  public IPersistentMap assoc(Object keyObj, Object value)
  {
    map.put(stringify(keyObj), value);
    return this;
  }

  public IPersistentMap assocEx(Object keyObj, Object value)
  {
    String key = stringify(keyObj);
    if(containsKey(key))
      throw new RuntimeException("Key already present");
    return assoc(key, value);
  }

  public IPersistentMap without(Object key)
  {
    map.remove(stringify(key));
    return this;
  }

  public Object valAt(Object o)
  {
    return valAt(o, null);
  }

  public Object valAt(Object keyObj, Object defaultValue)
  {
    final Object result = map.get(stringify(keyObj));
    if(result != null)
      return result;
    else
      return defaultValue;
  }

  private String stringify(Object keyObj)
  {
    return Opts.toKey(keyObj);
  }

  public int count()
  {
    return map.size();
  }

  public IPersistentCollection empty()
  {
    return PersistentHashMap.EMPTY;
  }

  public Iterator iterator()
  {
    return map.entrySet().iterator();
  }

  public ISeq seq()
  {
    if(map.size() > 0)
      return RT.seq(map);
    else
      return null;
  }
}
