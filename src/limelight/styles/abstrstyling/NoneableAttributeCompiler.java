//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.abstrstyling;

public class NoneableAttributeCompiler<A extends StyleValue> extends StyleCompiler
{
  private final StyleCompiler target;

  public NoneableAttributeCompiler(StyleCompiler target)
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
    String value = stringify(objValue);
    if("none".equals(value.toLowerCase()))
      return new NoneableValue<A>(null);
    else
      return new NoneableValue<A>((A)target.compile(value));
  }

  public StyleCompiler getTarget()
  {
    return target;
  }
}
