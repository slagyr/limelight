package limelight.styles.styling;

import limelight.styles.StyleAttribute;
import limelight.styles.StyleAttributeCompiler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PercentageAttributeCompiler extends StyleAttributeCompiler
{
  private static Pattern percentagePattern = Pattern.compile("(\\d+)%");

  public StyleAttribute compile(String value)
  {
    try
    {
      if(value.indexOf(".") != -1)
        return compileDecimalValue(value);
      else
        return compileIntValue(value);
    }
    catch(Exception e)
    {
      throw makeError(value);
    }
  }

  private StyleAttribute compileIntValue(String rawValue)
  {
    String value = removePercentageSymbol(rawValue);
    int intValue = Integer.parseInt(value);
    if(isValidRange(intValue))
      return new PercentageAttribute(intValue);
    else
      throw makeError(rawValue);
  }

  private PercentageAttribute compileDecimalValue(String value)
  {
    double doubleValue = Double.parseDouble(value);
    int intValue = (int) (doubleValue * 100);
    if(isValidRange(intValue))
      return new PercentageAttribute(intValue);
    else
      throw makeError(value);
  }

  private boolean isValidRange(int intValue)
  {
    return intValue >= 0 && intValue <= 100;
  }

  private String removePercentageSymbol(String value)
  {
    Matcher matcher = percentagePattern.matcher(value);
    if(matcher.matches())
      value = matcher.group(1);
    return value;
  }
}
