//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.*;
import limelight.styles.styling.*;
import limelight.LimelightError;
import limelight.Context;

public class RealStyleAttributeCompilerFactory implements StyleAttributeCompilerFactory
{
  public static void install()
  {
    if(Context.instance().styleAttributeCompilerFactory == null)
      Context.instance().styleAttributeCompilerFactory = new RealStyleAttributeCompilerFactory();
  }

  public StyleAttributeCompiler compiler(String type, String name)
  {
    StyleAttributeCompiler result = null;

    if("string".equals(type))
      result = new StringAttributeCompiler();
    else if("integer".equals(type))
      result = new IntegerAttributeCompiler();
    else if("pixels".equals(type))
      result = new PixelsAttributeCompiler();
    else if("color".equals(type))
      result = new ColorAttributeCompiler();
    else if("on/off".equals(type))
      result = new OnOffAttributeCompiler();
    else if("percentage".equals(type))
      result = new PercentageAttributeCompiler();
    else if("dimension".equals(type))
      result = new DimensionAttributeCompiler();
    else if("noneable simple dimension".equals(type))
      result = new NoneableAttributeCompiler<DimensionAttribute>(new SimpleDimensionAttributeCompiler());
    else if("degrees".equals(type))
      result = new DegreesAttributeCompiler();
    else if("fill strategy".equals(type))
      result = new FillStrategyAttributeCompiler();
    else if("font style".equals(type))
      result = new FontStyleAttributeCompiler();
    else if("horizontal alignment".equals(type))
      result = new HorizontalAlignmentAttributeCompiler();
    else if("vertical alignment".equals(type))
      result = new VerticalAlignmentAttributeCompiler();
    else if("noneable integer".equals(type))
      result = new NoneableAttributeCompiler<SimpleIntegerAttribute>(new IntegerAttributeCompiler());
    else if("noneable string".equals(type))
      result = new NoneableAttributeCompiler<StringAttribute>(new StringAttributeCompiler());
    else if("x-coordinate".equals(type))
      result = new XCoordinateAttributeCompiler();
    else if("y-coordinate".equals(type))
      result = new YCoordinateAttributeCompiler();
    else
      throw new LimelightError("Unknown StyleAttributeCompiler named " + type);

    result.setName(name);
    result.type = type;
    return result;
  }
}
