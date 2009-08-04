//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.styling.SimpleColorAttribute;
import limelight.util.Colors;
import limelight.LimelightError;

import java.awt.*;

public class ColorAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(Object objValue)
  {
    String value = objValue.toString();
    try
    {
      Color color = Colors.resolve(value);
      return new SimpleColorAttribute(color);
    }
    catch(LimelightError e)
    {
      throw makeError(value);
    }
  }
}
