package limelight.styles.abstrstyling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;

public class NoneableAttributeCompiler<A extends StyleAttribute> extends StyleAttributeCompiler
{
  private StyleAttributeCompiler target;

  public NoneableAttributeCompiler(StyleAttributeCompiler target)
  {
    this.target = target;
  }

  public void setName(String name)
  {
    super.setName(name);
    target.setName(name);
  }

  public StyleAttribute compile(String value)
  {
    if("none".equals(value.toLowerCase()))
      return new NoneableAttribute<A>(null);
    else
      return new NoneableAttribute<A>((A)target.compile(value));
  }

  public StyleAttributeCompiler getTarget()
  {
    return target;
  }
}
