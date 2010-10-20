package limelight.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Options
{
  public static void apply(Object subject, Map<String, Object> options)
  {
    final Class<? extends Object> klass = subject.getClass();
    final Method[] methods = klass.getMethods();
    final Set<String> keySet = new HashSet<String>(options.keySet());
    for(String key : keySet)
    {
      String setterName = StringUtil.camalize("set " + key);
      findAndInvokeMethod(setterName, subject, options, key, methods);
    }
  }

  private static void findAndInvokeMethod(String setterName, Object subject, Map<String, Object> options, String key, Method[] methods)
  {
    final Object value = options.get(key);
    for(Method method : methods)
    {
      if(setterName.equals(method.getName()))
      {
        try
        {
          method.invoke(subject, value);
          options.remove(key);
          break;
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

}
