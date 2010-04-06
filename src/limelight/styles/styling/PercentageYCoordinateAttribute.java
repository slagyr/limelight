//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.YCoordinateAttribute;

import java.awt.*;

public class PercentageYCoordinateAttribute extends SimplePercentageAttribute implements YCoordinateAttribute
{
  public PercentageYCoordinateAttribute(double percentage)
  {
    super(percentage);
  }

  public int getY(int consumed, Rectangle area)
  {

    return (int) ((getPercentage() * 0.01) * (double) area.height) + area.y;
  }
}
