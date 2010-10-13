//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

  public static String gsub(String value, Pattern regex, Gsuber gsuber)
  {
    StringBuffer buffer = new StringBuffer();
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
  private static Pattern separatorRegex = Pattern.compile("[_| ][a-z]");
  public static String titleize(String value)
  {
    value = gsub(value, mergeRegex, new Gsuber(){
      public String replacementFor(Matcher matcher)
      {
        final String match = matcher.group();
        return match.substring(0, 1) + " " + match.substring(1);
      }
    });

    value = gsub(value, separatorRegex, new Gsuber(){
      public String replacementFor(Matcher matcher)
      {
        final String match = matcher.group();
        return " " + match.substring(match.length() - 1).toUpperCase();
      }
    });

    return value.substring(0, 1).toUpperCase() + value.substring(1);
  }

//    # Converts Ruby style names to Java style camal case.
//  #
//  #   "four_score".camalized # => "FourScore"
//  #   "and_seven_years".camalized(:lower) # => "andSevenYears"
//  #
//  def camalized(starting_case = :upper)
//    value = self.downcase.gsub(/[_| |\-][a-z]/) { |match| match[-1..-1].upcase }
//    value = value[0..0].upcase + value[1..-1] if starting_case == :upper
//    return value
//  end
//
//  # Converts Java camel case names to ruby style underscored names.
//  #
//  #   "FourScore".underscored # => "four_score"
//  #   "andSevenYears".underscored # => "and_seven_years"
//  #
//  def underscored
//    value = self[0..0].downcase + self[1..-1]
//    value = value.gsub(/[A-Z]/) { |cap| "_#{cap.downcase}" }
//    return value
//  end
}
