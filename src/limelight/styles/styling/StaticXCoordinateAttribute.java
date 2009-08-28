//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.XCoordinateAttribute;

import java.awt.*;

public class StaticXCoordinateAttribute extends SimpleIntegerAttribute implements XCoordinateAttribute
{
  public StaticXCoordinateAttribute(int value)
  {
    super(value);
  }

  public int getX(int consumed, Rectangle area)
  {
    return getValue() + area.x;
  }
}
