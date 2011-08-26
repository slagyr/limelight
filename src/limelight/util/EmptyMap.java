//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.util;

import java.util.*;

public class EmptyMap<K,V> implements Map<K,V>
{
  public int size()
  {
    return 0;
  }

  public boolean isEmpty()
  {
    return true;
  }

  public boolean containsKey(Object key)
  {
    return false;
  }

  public boolean containsValue(Object value)
  {
    return false;
  }

  public V get(Object key)
  {
    return null;
  }

  public V put(K key, V value)
  {
    return null;
  }

  public V remove(Object key)
  {
    return null;
  }

  public void putAll(Map m)
  {
  }

  public void clear()
  {
  }

  public Set<K> keySet()
  {
    return Collections.emptySet();
  }

  public Collection<V> values()
  {
    return Collections.emptySet();
  }

  public Set<Map.Entry<K,V>> entrySet()
  {
    return Collections.emptySet();
  }
}
