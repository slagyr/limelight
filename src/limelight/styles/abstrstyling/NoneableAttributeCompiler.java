//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.abstrstyling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleValue;

public class NoneableAttributeCompiler<A extends StyleValue> extends StyleAttributeCompiler
{
  private final StyleAttributeCompiler target;

  public NoneableAttributeCompiler(StyleAttributeCompiler target)
  {
    this.target = target;
  }

  public void setName(String name)
  {
    super.setName(name);
    target.setName(name);
  }

  public StyleValue compile(Object objValue)
  {
    String value = objValue.toString();
    if("none".equals(value.toLowerCase()))
      return new NoneableValue<A>(null);
    else
      return new NoneableValue<A>((A)target.compile(value));
  }

  public StyleAttributeCompiler getTarget()
  {
    return target;
  }
}
