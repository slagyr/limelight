package limelight.styles.styling;

import limelight.styles.StyleAttributeCompiler;
import limelight.styles.StyleAttribute;

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
