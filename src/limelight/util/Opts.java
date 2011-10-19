//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.util;

import java.util.HashMap;
import java.util.Map;

public class Opts extends HashMap<String, Object>
{
  public Opts()
  {
    super();
  }

  public Opts(Map<Object, Object> starter)
  {
    this();
    for(Map.Entry<Object, Object> entry : starter.entrySet())
      put(toKey(entry.getKey()), entry.getValue());
  }

  public static Opts with(Object... args)
  {
    if(args.length % 2 == 1)
      throw new RuntimeException("OptionsMap.with must be called with an even number of parameters");
    Opts map = new Opts();
    for(int i = 0; i < args.length; i += 2)
    {
      String key = "" + args[i];
      Object value = args[i + 1];
      map.put(key, value);
    }
    return map;
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

  public Opts merge(Map<String, Object> other)
  {
    Opts result = (Opts)this.clone();
    for(Map.Entry<String, Object> entry : other.entrySet())
      result.put(entry.getKey(), entry.getValue());
    return result;
  }

  public Opts merge(Object... args)
  {
    return merge(Opts.with(args));
  }
}
