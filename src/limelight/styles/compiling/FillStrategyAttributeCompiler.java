//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleCompiler;
import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.values.*;

public class FillStrategyAttributeCompiler extends StyleCompiler
{
  public StyleValue compile(Object value)
  {
    String name = value.toString().toLowerCase();
    if("static".equals(name))
      return new StaticFillStrategyValue();
    else if("repeat".equals(name))
      return new RepeatFillStrategyValue();
    else if("repeat_x".equals(name) || "repeat-x".equals(name))
      return new RepeatXFillStrategyValue();
    else if("repeat_y".equals(name) || "repeat-y".equals(name))
      return new RepeatYFillStrategyValue();
    else if("scale".equals(name))
      return new ScaleFillStrategyValue();
    else if("scale_x".equals(name) || "scale-x".equals(name))
      return new ScaleXFillStrategyValue();
    else if("scale_y".equals(name) || "scale-y".equals(name))
      return new ScaleYFillStrategyValue();
    else
      throw makeError(value);
  }
}
