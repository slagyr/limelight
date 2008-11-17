package limelight.styles.styling;

import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.abstrstyling.StyleAttributeCompiler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PercentageAttributeCompiler extends StyleAttributeCompiler
{
  private static final Pattern percentagePattern = Pattern.compile("(\\d+)%");

  public StyleAttribute compile(Object objValue)
  {
    String value = objValue.toString();
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
      return new SimplePercentageAttribute(intValue);
    else
      throw makeError(rawValue);
  }

  private SimplePercentageAttribute compileDecimalValue(String value)
  {
    double doubleValue = Double.parseDouble(value);
    int intValue = (int) (doubleValue * 100);
    if(isValidRange(intValue))
      return new SimplePercentageAttribute(intValue);
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
