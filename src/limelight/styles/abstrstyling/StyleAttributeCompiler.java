//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.abstrstyling;

import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;

public abstract class StyleAttributeCompiler
{
  public String name;
  public String type;

  public abstract StyleAttribute compile(Object value);

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
}
