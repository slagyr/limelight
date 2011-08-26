//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.abstrstyling;

public abstract class StyleCompiler
{
  public String name;
  public String type;

  public abstract StyleValue compile(Object value);

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public InvalidStyleAttributeError makeError(Object invalidValue)
  {
    return new InvalidStyleAttributeError("Invalid value '" + invalidValue + "' for " + name + " style attribute.");
  }

  public static void installDefault()
  {

  }

  public static String stringify(Object value)
  {
    String result = value.toString();
    if(result.charAt(0) == ':')
      return result.substring(1);
    return result;
  }
}
