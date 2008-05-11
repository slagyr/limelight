package limelight.util;

public class StringUtil
{
  public static String join(String[] tokens, String delimiter)
  {
    if (tokens.length == 0)
      return "";

    StringBuffer joined = new StringBuffer();

    boolean first = true;
    for (String token : tokens)
    {
      if(!first)
        joined.append(delimiter);
      else
        first = false;
      joined.append(token);
    }

    return joined.toString();
  }
}
