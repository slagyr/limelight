//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

import java.util.HashMap;
import java.util.Map;

public class OptionsMap extends HashMap<String, Object>
{
  public OptionsMap()
  {
    super();
  }

  public OptionsMap(Map<Object, Object> starter)
  {
    this();
    for(Map.Entry<Object, Object> entry : starter.entrySet())
      put(toKey(entry.getKey()), entry.getValue());
  }

  @Override
  public Object get(Object key)
  {
    if(key == null)
      return null;
    return super.get(toKey(key));
  }

  @Override
  public Object put(String key, Object value)
  {
    if(key == null)
      return null;
    return super.put(toKey(key), value);
  }

  @Override
  public boolean containsKey(Object key)
  {
    return key != null && super.containsKey(toKey(key));
  }

  @Override
  public Object remove(Object key)
  {
    if(key == null)
      return null;
    return super.remove(toKey(key));
  }
  
  public static String toKey(Object key)
  {
    if(key == null)
      return null;
    else if(key instanceof String)
      return (String)key;
    else
    {
      String value = key.toString();
      if(value.startsWith(":"))
        return value.substring(1);
      else
        return value;
    }
  }

  public String inspect()
  {
    StringBuilder buffer = new StringBuilder("{ ");
    boolean first = true;
    for(Map.Entry<String, Object> entry : entrySet())
    {
      if(first)
        first = false;
      else
        buffer.append(", ");
      buffer.append(entry.getKey()).append(" => ").append(entry.getValue());
    }
    buffer.append(" }");
    return buffer.toString();
  }
}
