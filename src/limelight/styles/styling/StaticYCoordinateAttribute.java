//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.YCoordinateAttribute;

import java.awt.*;

public class StaticYCoordinateAttribute extends SimpleIntegerAttribute implements YCoordinateAttribute
{
  public StaticYCoordinateAttribute(int value)
  {
    super(value);
  }

  public int getY(int consumed, Rectangle area)
  {
    return getValue() + area.y;
  }
}
