package limelight.styles.abstrstyling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;

public class NoneableAttributeCompiler<A extends StyleAttribute> extends StyleAttributeCompiler
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

  public StyleAttribute compile(Object objValue)
  {
    String value = objValue.toString();
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
