package limelight.styles.styling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.util.Colors;
import limelight.LimelightError;

import java.awt.*;

public class ColorAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(String value)
  {
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
