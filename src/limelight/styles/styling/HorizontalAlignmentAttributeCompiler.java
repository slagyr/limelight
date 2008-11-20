package limelight.styles.styling;

import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.util.Aligner;

public class HorizontalAlignmentAttributeCompiler extends StyleAttributeCompiler
{
  public StyleAttribute compile(Object value)
  {
    String lowerCase = value.toString().toLowerCase().trim();
    if("left".equals(lowerCase))
      return new SimpleHorizontalAlignmentAttribute(Aligner.LEFT);
    else if("center".equals(lowerCase) || "middle".equals(lowerCase))
      return new SimpleHorizontalAlignmentAttribute(Aligner.HORIZONTAL_CENTER);
    else if("right".equals(lowerCase))
      return new SimpleHorizontalAlignmentAttribute(Aligner.RIGHT);
    else
      throw makeError(value);

  }
}
