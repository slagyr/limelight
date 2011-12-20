//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil
{
  public static String join(Object[] tokens, String delimiter)
  {
    if(tokens.length == 0)
      return "";

    StringBuilder joined = new StringBuilder();

    boolean first = true;
    for(Object token : tokens)
    {
      if(!first)
        joined.append(delimiter);
      else
        first = false;
      joined.append(token);
    }

    return joined.toString();
  }

  public static String join(String delimiter, Object... tokens)
  {
    return join(tokens, delimiter);
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

  public static String gsub(String value, Pattern regex, Gsuber gsuber)
  {
    StringBuilder buffer = new StringBuilder();
    Matcher matcher = regex.matcher(value);
    int lastMatchEndIndex = 0;
    while(matcher.find())
    {
      buffer.append(value.substring(lastMatchEndIndex, matcher.start()));
      buffer.append(gsuber.replacementFor(matcher));
      lastMatchEndIndex = matcher.end();
    }
    buffer.append(value.substring(lastMatchEndIndex));
    return buffer.toString();
  }

  public static interface Gsuber
  {
    String replacementFor(Matcher matcher);
  }

  private static Pattern mergeRegex = Pattern.compile("[a-z0-9][A-Z]");
  private static Pattern titleSeparatorRegex = Pattern.compile("[_| |\\-][a-z]");
  private static Pattern camelSeparatorRegex = Pattern.compile("[_| |\\-][A-Za-z]");
  private static Pattern underscoreSeparatorRegex = Pattern.compile("[ |\\-][A-Za-z]");
  private static Pattern snakeSeparatorRegex = Pattern.compile("[ |_][A-Za-z]");

  public static String titleCase(String value)
  {
    String result = separateMerges(value, " ");

    result = gsub(result, titleSeparatorRegex, new Gsuber()
    {
      public String replacementFor(Matcher matcher)
      {
        final String match = matcher.group();
        return " " + match.substring(match.length() - 1).toUpperCase();
      }
    });

    return result.substring(0, 1).toUpperCase() + result.substring(1);
  }

  public static String camelCase(String value)
  {
    return gsub(value, camelSeparatorRegex, new Gsuber()
    {
      public String replacementFor(Matcher matcher)
      {
        return matcher.group().substring(1).toUpperCase();
      }
    });
  }

  public static String capitalCamelCase(String value)
  {
    String result = camelCase(value);
    return result.substring(0, 1).toUpperCase() + result.substring(1);
  }

  public static String snakeCase(String value)
  {
    return changeCase(value, underscoreSeparatorRegex, "_");
  }

  public static String spearCase(String value)
  {
    return changeCase(value, snakeSeparatorRegex, "-");
  }

  private static String changeCase(String value, Pattern badSeparaterPattern, final String separator)
  {
    String result = separateMerges(value, separator);

    result = gsub(result, badSeparaterPattern, new StringUtil.Gsuber()
    {
      public String replacementFor(Matcher matcher)
      {
        return separator + matcher.group().substring(1).toLowerCase();
      }
    });
    return result.toLowerCase();
  }

  private static String separateMerges(String value, final String separator)
  {
    return gsub(value, mergeRegex, new StringUtil.Gsuber()
    {
      public String replacementFor(Matcher matcher)
      {
        final String match = matcher.group();
        return match.substring(0, 1) + separator + match.substring(1);
      }
    });
  }

}
