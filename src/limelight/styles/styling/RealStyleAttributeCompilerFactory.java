package limelight.styles.styling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttributeCompilerFactory;
import limelight.styles.abstrstyling.NoneableAttributeCompiler;
import limelight.styles.abstrstyling.StringAttribute;
import limelight.LimelightError;
import limelight.Context;

public class RealStyleAttributeCompilerFactory implements StyleAttributeCompilerFactory
{
  public static void install()
  {
    if(Context.instance().styleAttributeCompilerFactory == null)
      Context.instance().styleAttributeCompilerFactory = new RealStyleAttributeCompilerFactory();
  }

  public StyleAttributeCompiler compiler(String name)
  {
    if("string".equals(name))
      return new StringAttributeCompiler();
    else if("integer".equals(name))
      return new IntegerAttributeCompiler();
    else if("color".equals(name))
      return new ColorAttributeCompiler();
    else if("on/off".equals(name))
      return new OnOffAttributeCompiler();
    else if("percentage".equals(name))
      return new PercentageAttributeCompiler();
    else if("dimension".equals(name))
      return new DimensionAttributeCompiler();
    else if("degrees".equals(name))
      return new DegreesAttributeCompiler();
    else if("fill strategy".equals(name))
      return new FillStrategyAttributeCompiler();
    else if("font style".equals(name))
      return new FontStyleAttributeCompiler();
    else if("horizontal alignment".equals(name))
      return new HorizontalAlignmentAttributeCompiler();
    else if("vertical alignment".equals(name))
      return new VerticalAlignmentAttributeCompiler();
    else if("noneable integer".equals(name))
      return new NoneableAttributeCompiler<SimpleIntegerAttribute>(new IntegerAttributeCompiler());
    else if("noneable string".equals(name))
      return new NoneableAttributeCompiler<StringAttribute>(new StringAttributeCompiler());
    else
      throw new LimelightError("Unknown StyleAttributeCompiler named " + name);
  }
}
