//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

public class StringUtil
{
  public static String join(String delimiter, String... tokens)
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

  public static boolean endsWithNewline(String value)
  {
    // MDM - Is there a better way to detect newline chars?
    return value.endsWith("\n");
  }

  public static boolean isNewlineChar(char c)
  {
    return c == '\n' || c == '\r';
  }
}
