//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleCompiler;
import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.values.SimpleColorValue;
import limelight.util.Colors;
import limelight.LimelightError;

import java.awt.*;

public class ColorAttributeCompiler extends StyleCompiler
{
  public StyleValue compile(Object objValue)
  {
    String value = objValue.toString();
    try
    {
      Color color = Colors.resolve(value);
      return new SimpleColorValue(color);
    }
    catch(LimelightError e)
    {
      throw makeError(value);
    }
  }
}
