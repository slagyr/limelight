package limelight.styles.styling;

import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.abstrstyling.StyleAttributeCompiler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PercentageAttributeCompiler extends StyleAttributeCompiler
{
  private static final Pattern percentagePattern = Pattern.compile("(\\d+(.\\d+)?)%");

  public StyleAttribute compile(Object value)
  {
    try
    {
      if(value instanceof Number)
      {
        double doubleValue = ((Number)value).doubleValue();
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
    String value = removePercentageSymbol(rawValue);
    double doubleValue = Double.parseDouble(value);
    if(isValidRange(doubleValue))
      return new SimplePercentageAttribute(doubleValue);
    else
      throw makeError(value);
  }

  private boolean isValidRange(double value)
  {
    return value >= 0 && value <= 100;
  }

  private String removePercentageSymbol(String value)
  {
    Matcher matcher = percentagePattern.matcher(value);
    if(matcher.matches())
      value = matcher.group(1);
    return value;
  }
}
