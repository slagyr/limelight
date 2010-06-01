//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import limelight.styles.abstrstyling.DegreesValue;

public class SimpleDegreesValue extends SimpleIntegerValue implements DegreesValue
{
  public SimpleDegreesValue(int degrees)
  {
    super(degrees);
  }

  public int getDegrees()
  {
    return getValue();
  }
}
