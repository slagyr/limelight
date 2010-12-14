package limelight.styles;

import java.util.HashMap;
import java.util.Map;

public class Styles
{
  public static HashMap<String, RichStyle> merge(Map<String, RichStyle> newStyles, HashMap<String, RichStyle> extendableStyles)
  {
    final HashMap<String, RichStyle> result = new HashMap<String, RichStyle>(extendableStyles);
    for(Map.Entry<String, RichStyle> entry : newStyles.entrySet())
    {
      String name = entry.getKey();
      RichStyle value = entry.getValue();
      if(result.containsKey(name))
      {
        value.addExtension(result.get(name));
        result.put(name, value);
      }
      else
        result.put(name, value);
    }
    return result;
  }
}
