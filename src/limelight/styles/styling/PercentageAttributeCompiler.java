//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.DimensionAttribute;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PercentageAttributeCompiler extends StyleAttributeCompiler
{
  private static final Pattern percentagePattern = Pattern.compile("(\\d+(.\\d+)?)%?");

  public StyleAttribute compile(Object value)
  {
    try
    {
      if(value instanceof Number)
      {
        double doubleValue = ((Number) value).doubleValue();
        return new SimplePercentageAttribute(doubleValue);
      }
      else
        return compileString(value.toString());
    }
    catch(Exception e)
    {
      throw makeError(value);
    }
  }

  private SimplePercentageAttribute compileString(String rawValue)
  {
    double doubleValue = convertToDouble(rawValue);
    if(doubleValue >= 0)
      return new SimplePercentageAttribute(doubleValue);
    else
      throw makeError(rawValue);
  }

  public static double convertToDouble(String value)
  {
    Matcher matcher = percentagePattern.matcher(value);
    if(matcher.matches())
    {
      String percentStringValue = matcher.group(1);
      double percentValue = Double.parseDouble(percentStringValue);
      if(percentValue >= 0 && percentValue <= 100)
        return percentValue;
    }
    return -1.0;
  }

  public static boolean isPercentage(String value)
  {
    return value.indexOf('%') != -1;
  }
}
