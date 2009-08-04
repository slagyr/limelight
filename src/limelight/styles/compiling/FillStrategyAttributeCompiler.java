//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.styling.*;

public class FillStrategyAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(Object value)
  {
    String name = value.toString().toLowerCase();
    if("static".equals(name))
      return new StaticFillStrategyAttribute();
    else if("repeat".equals(name))
      return new RepeatFillStrategyAttribute();
    else if("repeat_x".equals(name) || "repeat-x".equals(name))
      return new RepeatXFillStrategyAttribute();
    else if("repeat_y".equals(name) || "repeat-y".equals(name))
      return new RepeatYFillStrategyAttribute();
    else if("scale".equals(name))
      return new ScaleFillStrategyAttribute();
    else if("scale_x".equals(name) || "scale-x".equals(name))
      return new ScaleXFillStrategyAttribute();      
    else if("scale_y".equals(name) || "scale-y".equals(name))
      return new ScaleYFillStrategyAttribute();
    else
      throw makeError(value);
  }
}
