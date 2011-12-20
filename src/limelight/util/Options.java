//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Options
{
  public static void apply(Object subject, Map<String, Object> options)
  {
    final Class<? extends Object> klass = subject.getClass();
    final List<Method> methods = getSetters(klass);
    final Set<String> keySet = new HashSet<String>(options.keySet());
    for(String key : keySet)
    {
      String setterName = StringUtil.camelCase("set " + key);
      findAndInvokeMethod(setterName, subject, options, key, methods);
    }
  }

  private static List<Method> getSetters(Class<? extends Object> klass)
  {
    ArrayList<Method> setters = new ArrayList<Method>();
    for(Method method : klass.getMethods())
    {
      if(method.getName().startsWith("set"))
        setters.add(method);
    }
    return setters;
  }

  private static void findAndInvokeMethod(String setterName, Object subject, Map<String, Object> options, String key, List<Method> methods)
  {
    for(Method method : methods)
    {
      if(setterName.equals(method.getName()))
      {
        try
        {
          final Object value = options.get(key);
          final Class<?>[] paramTypes = method.getParameterTypes();
          final Class<?> paramType = paramTypes[0];
          final Object coercedValue = coerce(value, paramType);
          method.invoke(subject, coercedValue);
          options.remove(key);
          return;
        }
        catch(IllegalArgumentException e)
        {
          // Do Nothing
        }
        catch(IllegalAccessException e)
        {
          // Do Nothing
        }
        catch(InvocationTargetException e)
        {
          // Do Nothing
        }
      }
    }
  }

  private static Object coerce(Object obj, Class<?> type)
  {
    if(obj instanceof String && String.class != type)
    {
      String value = obj.toString();
      if(type == Boolean.TYPE || type == Boolean.class)
        return "true".equals(value.toLowerCase()) || "on".equals(value.toLowerCase());
      else if(type == Byte.TYPE || type == Byte.class)
        return Byte.valueOf(value);
      else if(type == Short.TYPE || type == Short.class)
        return Short.valueOf(value);
      else if(type == Integer.TYPE || type == Integer.class)
        return Integer.valueOf(value);
      else if(type == Long.TYPE || type == Long.class)
        return Long.valueOf(value);
      else if(type == Float.TYPE || type == Float.class)
        return Float.valueOf(value);
      else if(type == Double.TYPE || type == Double.class)
        return Double.valueOf(value);
      else if(type == java.util.Collection.class)
      {
        final String[] parts = value.split(",");
        ArrayList<Object> result = new ArrayList<Object>();
        for(String part : parts)
          result.add(part.trim());
        return result;
      }
      else
        return obj;
    }
    else
      return obj;
  }

}
